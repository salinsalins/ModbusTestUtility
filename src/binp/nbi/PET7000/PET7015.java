/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binp.nbi.PET7000;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;

/**
 *
 * @author sanin
 */
public class PET7015 extends PET7000 {
    static final Logger logger = Logger.getLogger(PET7015.class.getName());
    int[] channelType = new int[7];
    int[] cti = new int[7];
    
    public PET7015(String strAddr, int port, int c) throws UnknownHostException, Exception {
        super(strAddr, port, c);
        if(moduleName != 0x7015) {
            System.out.printf("Incorrect module name: 0x%H\n", moduleName);
        } else {
            System.out.printf("Module name: 0x%4H\n", moduleName);
            channelType = readMultipleRegisters(427, 7);
            cti = new int[channelType.length];
            for (int i=0; i < channelType.length; i++) {
                cti[i] = typeIndex(channelType[i]);
                System.out.printf("Channel %d", i);
                System.out.printf(" - %s\n", typeName(channelType[i]));
            }
        }
    }
    
    public PET7015(String strAddr) throws UnknownHostException, Exception {
        this(strAddr, Modbus.DEFAULT_PORT, 1);
    }

    public double[] read() {
        double[] result = new double[7];
        try {
            int[] registers = readInputRegisters(0, 7);
            for (int i = 0; i < registers.length; i++) {
                int index = cti[i];
                result[i] = scaleMax[index]/0x7fff*registers[i];
                if(registers[i] == 0x8000) result[i] = -9999.9;
                System.out.println("read: "+i+" "+registers[i]+" "+result[i]);
            }
            return result;
        } catch (ModbusException ex) {
            logger.log(Level.WARNING, "ModbusException", ex);
            for (int i = 0; i < result.length; i++) {
                result[i] = -9999.9;
            }
            return result;
        }
    }

    public double read(int channel) {
        try {
            int[] register = readInputRegisters(channel, 1);
            int index = cti[channel];
            double result;
            result = scaleMax[index]/0x7fff*register[0];
            if(register[0] == 0x8000) result = -9999.9;
            return result;
        } catch (ModbusException ex) {
            logger.log(Level.WARNING, "ModbusException", ex);
            return -9999.9;
        }
    }
}
