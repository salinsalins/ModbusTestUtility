/*
 * Copyright (c) 2016, Andrey Sanin. All rights reserved.
 *
 */

package binp.nbi.modbustest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import binp.nbi.tango.util.datafile.DataFile;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import jssc.SerialPort;
import jssc.SerialPortList;
import java.net.*;
import java.io.*;
import java.util.Date;
import javax.swing.SpinnerListModel;
import net.wimpi.modbus.*;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.io.*;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.util.*;
 

public class ModbusTestUtility extends javax.swing.JFrame implements WindowListener {
    static final Logger logger = Logger.getLogger(ModbusTestUtility.class.getName());

    InetAddress addr = null; //the slave's address
    int port = Modbus.DEFAULT_PORT;
    int unitid = 1; //the unit identifier we will be talking to
    int ref = 0;    //the reference; offset where to start reading from
    int count = 1;  //the number of DI's or AI's to read
    
    TCPMasterConnection con = null;         //the connection
    ModbusTCPTransaction trans = null;      //the transaction
    //ReadInputDiscretesRequest req = null; //the request
    //ReadInputDiscretesResponse res = null; //the response
    ModbusRequest req = null;
    ModbusResponse res = null;

    /**
     * Creates new form BeamProfile
     */
    public ModbusTestUtility() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jSpinner4 = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calorimeter Beam Profile Plotter");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Config"));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "192.168.1.202" }));
        jComboBox1.setToolTipText("");

        jLabel3.setText("Function:");

        jLabel6.setText("URL:");

        jLabel1.setText("Port:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(502, 0, null, 1));

        jButton1.setText("Execute Command");
        jButton1.setActionCommand("Execute");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Unit ID:");

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(1, 0, 255, 1));

        jLabel5.setText("Count:");

        jSpinner5.setModel(new javax.swing.SpinnerListModel(new String[] {" (0x01) — Read Coil Status.", " (0x02) — Read Discrete Inputs.", " (0x03) — Read Holding Registers.", " (0x04) — Read Input Registers."}));

        jLabel7.setText("Referece:");

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(1, 0, 255, 1));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Responce"));

        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTextArea3.setRows(5);
        jScrollPane5.setViewportView(jTextArea3);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Errors"));

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTextArea2.setRows(5);
        jScrollPane4.setViewportView(jTextArea2);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton2.setText("Connect");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.setText("Not connected");

        jLabel8.setText("Connection Status:");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Rquest"));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 415, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinner5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 217, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton1)
                        .add(437, 437, 437))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel8)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextField1))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(10, 10, 10)
                                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 201, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jButton2))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSpinner3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(jLabel7)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSpinner6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jLabel5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSpinner4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 441, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1)
                    .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jSpinner3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7)
                    .add(jSpinner6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5)
                    .add(jSpinner4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jSpinner5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(11, 11, 11)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, 0, 466, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            //1. Setup the parameters
            unitid =  (int) jSpinner3.getValue();
            ref = (int) jSpinner6.getValue();
            count =  (int) jSpinner4.getValue();;

            String fs = (String) jSpinner5.getValue();
            List fl = ((SpinnerListModel) jSpinner5.getModel()).getList();
            int index = fl.indexOf(fs);

            //3. Prepare the request
            if( index == 0)
                req = new ReadCoilsRequest(ref, count);
            if( index == 1)
                req = new ReadInputDiscretesRequest(ref, count);
            if( index == 2)
                req = new ReadMultipleRegistersRequest(ref, count);
            if( index == 3)
                req = new ReadInputRegistersRequest(ref, count);

            req.setUnitID(unitid);
            //req.setHeadless();
            System.out.println(req);
            System.out.println(req.getDataLength());
            System.out.println(req.getFunctionCode());
            System.out.println(req.getHexMessage());

            //4. Prepare the transaction
            trans = new ModbusTCPTransaction(con);
            trans.setRequest(req);    

            //5. Execute the transaction
            trans.execute();

            if( index == 0)
                res = (ReadCoilsResponse)trans.getResponse();
            if( index == 1)
                res = (ReadInputDiscretesResponse) trans.getResponse();
            if( index == 2)
                res = (ReadMultipleRegistersResponse) trans.getResponse();
            if( index == 3)
                res = (ReadInputRegistersResponse) trans.getResponse();

            System.out.println(res);
            System.out.println(res.getDataLength());
            System.out.println(res.getFunctionCode());
            System.out.println(res.getHexMessage());
            jTextArea1.setText("Response\n");
            jTextArea1.append(res.getHexMessage() + "\n");

        } catch (Exception ex) {
            jTextArea1.setText("- Error -");
            jTextArea2.setText("Exception " + ex);
            logger.log(Level.SEVERE, "Exception", ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Open the connection
        try {
            //addr = InetAddress.getByName("192.168.1.202");
            addr = InetAddress.getByName((String) jSpinner5.getValue());
            con = new TCPMasterConnection(addr);
            port = (int) jSpinner1.getValue();
            con.setPort(port);
            con.connect();
            jTextField1.setText("Connected");
        } catch (UnknownHostException ex) {
            jTextField1.setText("Unknown Host Exception " + ex);
            logger.log(Level.WARNING, "Unknown Host Exception", ex);
        } catch (Exception ex) {
            jTextField1.setText("Connection Error " + ex);
            logger.log(Level.WARNING, "Connection Error", ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private static Date lastDate = new Date();
    public void mark() {
        Date date = new Date();
        System.out.printf("%d %tT.%2$tL %d\n", count++, date, date.getTime()-lastDate.getTime());
        lastDate= date;
    }
    public void mark(int c) {
        count = c;
        mark();
    }
    public void mark(String s) {
        System.out.printf("%s ", s);
        mark();
    }
    
    public static String readURL(String urlName) throws MalformedURLException, IOException {
        String result = "";
        // Create a URL 
        URL urlToRead = new URL(urlName);
        // Read and the URL characterwise
        // Open the streams
        InputStream inputStream = urlToRead.openStream();
        int c = inputStream.read();
        while (c != -1) {
            //System.out.print((char) c);
            result += (char) c;
            c = inputStream.read();
        }
        inputStream.close();
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ModbusTestUtility().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    
    private void restoreConfig() {
//        String logFileName = null;
//        List<String> columnNames = new LinkedList<>();
//        try {
//            ObjectInputStream objIStrm = new ObjectInputStream(new FileInputStream("config.dat"));
//
//            Rectangle bounds = (Rectangle) objIStrm.readObject();
//            frame.setBounds(bounds);
//
//            logFileName = (String) objIStrm.readObject();
//            txtFileName.setText(logFileName);
//            fileLog = new File(logFileName);
//
//            String str = (String) objIStrm.readObject();
//            folder = str;
//
//            str = (String) objIStrm.readObject();
//            txtarExcludedColumns.setText(str);
//
//            str = (String) objIStrm.readObject();
//            txtarIncludedColumns.setText(str);
//
//            boolean sm = (boolean) objIStrm.readObject();
//            chckbxShowMarkers.setSelected(sm);
//
//            boolean sp = (boolean) objIStrm.readObject();
//            chckbxShowPreviousShot.setSelected(sp);
//            
//            columnNames = (List<String>) objIStrm.readObject();
//
//            objIStrm.close();
//
//            logger.info("Config restored.");
//        } catch (IOException | ClassNotFoundException e) {
//            logger.log(Level.WARNING, "Config read error {0}", e);
//        }
//        timer.cancel();
//        timer = new Timer();
//        timerTask = new DirWatcher(window);
//        timer.schedule(timerTask, 2000, 1000);
//
//        logViewTable.readFile(logFileName);
//        logViewTable.setColumnNames(columnNames);
//        columnNames = logViewTable.getColumnNames();
//        // Add event listener for logview table
//        ListSelectionModel rowSM = logViewTable.getSelectionModel();
//        rowSM.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent event) {
//                //Ignore extra messages.
//                if (event.getValueIsAdjusting()) {
//                    return;
//                }
//
//                ListSelectionModel lsm = (ListSelectionModel) event.getSource();
//                if (lsm.isSelectionEmpty()) {
//                    //System.out.println("No rows selected.");
//                } else {
//                    int selectedRow = lsm.getMaxSelectionIndex();
//                    //System.out.println("Row " + selectedRow + " is now selected.");
//                    //String fileName = folder + "\\" + logViewTable.files.get(selectedRow);
//                    try {
//                        File zipFile = logViewTable.files.get(selectedRow);
//                        readZipFile(zipFile);
//                        if (timerTask != null && timerTask.timerCount > 0) {
//                            dimLineColor();
//                        }
//                    } catch (Exception e) {
//                        logger.log(Level.WARNING, "Selection change exception ", e);
//                        //panel.removeAll();
//                    }
//                }
//            }
//        });
//        logViewTable.clearSelection();
//        logViewTable.changeSelection(logViewTable.getRowCount()-1, 0, false, false);
   }

    private void saveConfig() {
//        timer.cancel();
//
//        Rectangle bounds = frame.getBounds();
//        String txt = txtFileName.getText();
//        txt = fileLog.getAbsolutePath();
//        String txt1 = txtarExcludedColumns.getText();
//        String txt2 = txtarIncludedColumns.getText();
//        boolean sm = chckbxShowMarkers.isSelected();
//        boolean sp = chckbxShowPreviousShot.isSelected();
//        List<String> columnNames = logViewTable.getColumnNames();
//        try {
//            ObjectOutputStream objOStrm = new ObjectOutputStream(new FileOutputStream("config.dat"));
//            objOStrm.writeObject(bounds);
//            objOStrm.writeObject(txt);
//            objOStrm.writeObject(folder);
//            objOStrm.writeObject(txt1);
//            objOStrm.writeObject(txt2);
//            objOStrm.writeObject(sm);
//            objOStrm.writeObject(sp);
//            objOStrm.writeObject(columnNames);
//            objOStrm.close();
//            logger.info("Config saved.");
//        } catch (IOException e) {
//            logger.log(Level.WARNING, "Config write error ", e);
//        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        saveConfig();
        //System.exit(0);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        restoreConfig();
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }


    class Task extends SwingWorker<Void, Void> {

        DataFile outputFile;
        boolean newOutput = false;
	
// Data arrays for traces
        int nx = 2000;
	int ny = 4*8+1;
	double[][] data = new double[nx][ny];
	double[] dmin = new double[ny];
	double[] dmax = new double[ny];

// Profile arrays and their plot handles
	int[] p1range = {2,3,4,5,6,7,10,11,12,13,14};    // Channels for vertical profile
	double[] p1x = {1,3,4,5,6,7,8,9,10,11,13};       // X values for prof1
	int[] p2range = {16, 7, 15};                     // Channels for horizontal profile
	int[] p2x = {3, 7, 11};                          // X values for prof2
	double[] prof1  = new double[p1range.length];    // Vertical profile and handle
	double prof1h = 0;
	double[] prof2  = new double[p2range.length];    // Horizontal profile
	double prof2h = 0;
	double[] prof1max  = prof1.clone();      // Maximal vertical profile (over the plot)
        //Arrays.fill(prof1max, 1.0);
        double prof1maxh = 0;          // Maximal vertical profile handle
	double[] prof1max1  = prof1max.clone();  // Maximal vertical profile (from the program start)
	double prof1max1h = 0;         // Handle
	double[] prof2max  = prof2.clone();      // Maximal horizontal profile (ofer the plot)
	//Arrays.fill(prof2max, 1.0);
	double prof2maxh = 0;
        
        
        Task() {
            //outputFile = new DataFile("log.txt");
            newOutput = true;
        }
    
        /**
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            // Initialize progress property.
            while(jToggleButton1.isSelected()) {
                //readData();
                //plotData();
            }
            return null;
        }

        /**
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            //taskOutput.append("Done!\n");
        }
    }

}
