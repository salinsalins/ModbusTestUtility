/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binp.nbi.PET7000;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.io.ModbusTransaction;
import net.wimpi.modbus.io.ModbusUDPTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteMultipleCoilsRequest;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.net.UDPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.BitVector;

/**
 *
 * @author sanin
 */
public class PET7000 {
    static final String[] typeNames = {
"0x20: Platinum 100, α=0.00385, -100°C ~ 100°C",
"0x21: Platinum 100, α=0.00385, 0°C ~ 100°C",
"0x22: Platinum 100, α=0.00385, 0°C ~ 200°C",
"0x23: Platinum 100, α=0.00385, 0°C ~ 600°C",
"0x24: Platinum 100, α=0.003916, -100°C ~ 100°C",
"0x25: Platinum 100, α=0. 003916, 0°C ~ 100°C",
"0x26: Platinum 100, α=0. 003916, 0°C ~ 200°C",
"0x27: Platinum 100, α=0. 003916, 0°C ~ 600°C",
"0x28: Nickel 120, -80°C ~ 100°C",
"0x29: Nickel 120, 0°C ~ 100°C",
"0x2A: Platinum 1000, α=0. 00385, -200°C ~ 600°C",
"0x2B: Cu 100 @ 0°C, α=0. 00421, -20°C ~ 150°C",
"0x2C: Cu 100 @ 25°C, α=0. 00427, 0°C ~ 200°C",
"0x2D: Cu 1000 @ 0°C, α=0. 00421, -20°C ~ 150°C",
"0x2E: Platinum 100, α=0. 00385, -200°C ~ 200°C",
"0x2F: Platinum 100, α=0. 003916, -200°C ~ 200°C",
"0x80: Platinum 100, α=0. 00385, -200°C ~ 600°C",
"0x81: Platinum 100, α=0. 003916, -200°C ~ 600°C",
"0x82: Cu 50 @ 0°C, -50°C ~ 150°C",
"0x83: Nickel 100, -60°C ~ 180°C",
"Unknown type"};
    static final int[] types =          { 0x20,   0x21,   0x22,   0x23,   0x24,   0x25,   0x26, 
          0x27,   0x28,   0x29,   0x2A,   0x2B,   0x2C,   0x2D,   0x2E,   0x2F,   0x80,   0x81,
          0x82,   0x83  };
    static final double[] scaleMin =  { -100.0, -100.0, -200.0, -600.0, -100.0, -100.0, -200.0, 
        -600.0, -100.0, -100.0, -600.0, -150.0, -200.0, -150.0, -200.0, -200.0, -600.0, -600.0,
        -150.0, -180.0, -1.0};
    static final double[] scaleMax  = {  100.0,  100.0,  200.0,  600.0,  100.0,  100.0,  200.0, 
         600.0,  100.0,  100.0,  600.0,  150.0,  200.0,  150.0,  200.0,  200.0,  600.0,  600.0,
         150.0,  180.0,  1.0};
    
    // Module address
    InetAddress addr = null;            //the slave's IP address
    int port = Modbus.DEFAULT_PORT;     //the  slave's port
    int unitID = 1;                     //the unit identifier we will be talking to (1 is default)

    TCPMasterConnection TCPMcon = null;     //the connection
    UDPMasterConnection UDPMcon = null;     //the connection
    int con = 1;

    ModbusTransaction trans = null;     //the last transaction
    // Transaction parameters
    int ref = 0;            //the reference; offset where to start reading from
    int count = 1;          //the number of DI's or AI's to read
    
    int moduleName = 0;
    
    public PET7000(String strAddr, int port, int c) throws UnknownHostException, Exception {
        setInetAddress(strAddr);
        setPort(port);
        openConnection(c);

        moduleName = readMultipleRegisters(559, 1)[0];
    }
    
    public PET7000(String strAddr) throws UnknownHostException, Exception {
        this(strAddr, Modbus.DEFAULT_PORT, 1);
    }

    public final int typeIndex(int type) {
        for(int i = 0; i < types.length; i++) {
            if(types[i] == type) {
                return i;
            }
        }
        return types.length;
    }
    
    public final String typeName(int type) {
        return typeNames[typeIndex(type)];
    }
    
    public final double typeScale(int type) {
        return scaleMax[typeIndex(type)];
    }
    
    public void setInetAddress(String strAddr) throws UnknownHostException {
        addr  = InetAddress.getByName(strAddr);
    }
    
    public void setPort(int newPort) {
        port  = newPort;
    }
    
    public void openConnection() throws Exception {
        closeConnection();
        if(1 == con) {
            TCPMcon = new TCPMasterConnection(addr);
            TCPMcon.setPort(port);
            TCPMcon.connect();
        }
        if(2 == con) {
            UDPMcon = new UDPMasterConnection(addr);
            UDPMcon.setPort(port);
            UDPMcon.connect();
        }
    }

    public void openConnection(int c) throws Exception {
        closeConnection();
        con = c;
        openConnection();
    }

    public void closeConnection() {
        if(1 == con) {
            if(TCPMcon != null)
                TCPMcon.close();
        }
        if(2 == con) {
            if(UDPMcon != null)
                UDPMcon.close();
        }
    }
    
    public boolean isConnected() {
        if(1 == con) {
            return TCPMcon.isConnected();
        }
        if(2 == con) {
            return UDPMcon.isConnected();
        }
        return false;
    }

    public ModbusResponse getResponse() {
        return trans.getResponse();
    }
    
    public void executeTransaction(ModbusRequest req) throws ModbusException {
        req.setUnitID(unitID);
        if(1 == con) {
            trans  = new ModbusTCPTransaction(TCPMcon);
        }
        if(2 == con) {
            trans  = new ModbusUDPTransaction(UDPMcon);
        }
        trans.setRequest(req);
        trans.execute();
    }

    public final int[] readMultipleRegisters(int ref, int count) throws ModbusException {
        ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(ref, count);
        executeTransaction(req);

        ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();
        int[] registers = new int[res.getWordCount()];
        for (int i = 0; i < res.getWordCount(); i++) {
            registers[i] = res.getRegisterValue(i);
        }
        return registers;
    }

    public final int[] readInputRegisters(int ref, int count) throws ModbusException {
        ReadInputRegistersRequest req = new ReadInputRegistersRequest(ref, count);
        executeTransaction(req);

        ReadInputRegistersResponse res = (ReadInputRegistersResponse) trans.getResponse();
        int[] registers = new int[res.getWordCount()];
        for (int i = 0; i < res.getWordCount(); i++) {
            registers[i] = res.getRegisterValue(i);
            if(registers[i] > 32768) 
                registers[i] |= 0xFFFF0000;  
        }
        return registers;
    }

    public final boolean[] readCoils(int ref, int count) throws ModbusException {
        ReadCoilsRequest req = new ReadCoilsRequest(ref, count);
        executeTransaction(req);

        ReadCoilsResponse res = (ReadCoilsResponse) trans.getResponse();
        boolean[] coils = new boolean[res.getBitCount()];
        for (int i = 0; i < res.getBitCount(); i++) {
            coils[i] = res.getCoilStatus(i);
        }
        return coils;
    }

    public final boolean[] readInputDiscretes(int ref, int count) throws ModbusException {
        ReadInputDiscretesRequest req = new ReadInputDiscretesRequest(ref, count);
        executeTransaction(req);

        ReadInputDiscretesResponse res = (ReadInputDiscretesResponse) trans.getResponse();
        boolean[] bits = new boolean[res.getBitCount()];
        for (int i = 0; i < res.getBitCount(); i++) {
            bits[i] = res.getDiscreteStatus(i);
        }
        return bits;
    }

    public final void write(int ref, int[] values) throws  ModbusException {
        Register[] regs = new SimpleRegister[values.length];
        for(int i=0; i < values.length; i++) {
            regs[i] = new SimpleRegister(values[i]);
        }
        WriteMultipleRegistersRequest req = new WriteMultipleRegistersRequest(ref, regs);
        executeTransaction(req);
    }

    public final void write(int ref, boolean[] values) throws  ModbusException {
        BitVector bv = new BitVector(values.length);
        for(int i=0; i < values.length; i++) {
            bv.setBit(i, values[i]);
        }
        WriteMultipleCoilsRequest req = new WriteMultipleCoilsRequest(ref, bv);
        executeTransaction(req);
    }

    public final void write(int ref, boolean value) throws  ModbusException {
        WriteCoilRequest req = new WriteCoilRequest(ref, value);
        executeTransaction(req);
    }

    public final void write(int ref, int value) throws  ModbusException {
        Register reg = new SimpleRegister(value);
        WriteSingleRegisterRequest req = new WriteSingleRegisterRequest(ref, reg);
        executeTransaction(req);
    }
}
