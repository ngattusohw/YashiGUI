import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import theproject.TheData;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.Timer;
import javax.swing.table.TableRowSorter;
import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class TheProject extends javax.swing.JFrame implements WindowListener , ClipboardOwner{
    // Variables declaration                    
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1,check,all,force,print;
    private javax.swing.JLabel jLabel1,textL;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private ArrayList<TheData> thedata;
    private DefaultTableModel model;
    private JTable jTable1;
    private Container pane;
    private JButton thebutton,thepushb;
    private boolean saved=true;
    private Timer t;
    private WritableWorkbook ww;
    private WritableSheet sheet;
    private Number num;
    private Label label,label1,label2,label3,label4;
/////////////////////////////////////////////////////////////////// End of variables declaration         
       
    public TheProject(){
////////////////////////////////////////////////////////////////////creates pane, adds buttons to the pane and creates the buttons        
        setSize(720,480);
        addWindowListener(this);
        pane=getContentPane();
        thedata=getData();//calls the getData method to retrive the needed data in the database
        thebutton = new JButton("Update List");
        thepushb = new JButton("Push Data");
        check= new JButton("Refresh Status");
        force= new JButton("Force Quit");
        all= new JButton("Contacted All");
        print = new JButton("Print to Excel");
        textL = new JLabel();
        pane.add(thebutton);
        pane.add(thepushb);
        pane.add(check);
        pane.add(force);
        pane.add(all);
        pane.add(print);
        pane.add(textL);
        pane.setBackground(new java.awt.Color(240, 240, 240));
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////sets the size and location for JButtons
        thebutton.setLocation(35,100);
        thebutton.setSize(115,25);
        thepushb.setLocation(35,175);
        thepushb.setSize(115,25);
        check.setLocation(35,250);
        check.setSize(115,25);
        all.setLocation(35,325);
        all.setSize(115,25);
        print.setLocation(35,400);
        print.setSize(115,25);
        force.setLocation(35,475);
        force.setSize(115,25);
        textL.setLocation(250,-35);
        textL.setSize(400,200);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////Get info for JLabel and then set that text      
        if(thedata==null) 
            textL.setText("<html>Database Status :<font color='red'> NOT CONNECTED! </font></html>");
        else
            textL.setText("<html>Database Status :<font color='green'> CONNECTED! </font></html>");
        
        textL.setFont(new Font("Arial", Font.PLAIN, 18));
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////Sets (row,col) data needed for the JTable
        long l=10;//
        Object[] columnName = {"Contacted","Title","Email","Id","Installs"};
        Object[][] data={{false,"Testing","hi@gmail.com", new Integer(123),l}};
        //Sets the type of each column in the JTable
        model=new DefaultTableModel(data,columnName) {
        @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
            public boolean isCellEditable(int row, int column){//gets called whenever a cell in the Jtable is click, sets so that only contacted_yet field and email fields can be accessed
                if(column==0){ 
                    saved=false;
                    return true;
                }
                return false;
            }
        };  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adds scrollPane, adds it to the pane, then adds the needed data from the data base to the JTable
        jTable1=new JTable(model);
        JScrollPane scrollPaneAS = new JScrollPane(jTable1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.add(scrollPaneAS);
        scrollPaneAS.setBounds(164, 100, 595, 476);
        jTable1.setColumnSelectionAllowed(true);
        /////
        long ah=23;
        thedata= new ArrayList<TheData>();
        thedata.add(new TheData(true,"HI","yo@wat.com",234,ah));
        thedata.add(new TheData(true,"BYE","yo@no.com",274,ah));
        thedata.add(new TheData(true,"FLY","hi@gmail.com",789,ah));
        thedata.add(new TheData(true,"bill NYE","yeet@w.com",456,ah));
        //thedata.add(new TheData(true,"HI","yo@wat.com",234,ah));
        //if(thedata!=null){//makes sure not to add null data to the Jtable to avoid crashing bc null pointer exception
            for(TheData hold : thedata){
                model.addRow(new Object[]{hold.getCon(),hold.getTitle(),hold.getEmail(),hold.getId(),hold.getInstalls()});
            }
       // }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //logic for buttons..
        thebutton.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
              setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
              thedata=getData();
              
              if(thedata!=null){
                for(int x=model.getRowCount()-1; x>0; x--){//delete all the current rows to ensure no duplicates in the data field
                    model.removeRow(x);
                }
                model.removeRow(0);
                for(TheData d: thedata){ //add new data back into jtable..
                    model.addRow(new Object[]{d.getCon(),d.getTitle(),d.getEmail(),d.getId(),d.getInstalls()});
                    }
                JOptionPane.showMessageDialog(null, "Update finished..");
              }    
              setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          }
        });
       
        thepushb.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if(UpdateD())//if the data was pushed successfully then saved=true: able to close window, otherwise not able to close
                    saved=true;
                else
                    saved=false;
                
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
        });
        
        check.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                TheConnection intc = new TheConnection();
                Connection incon = intc.getConnection();
                if(incon==null) 
                    textL.setText("<html>Database Status :<font color='red'> NOT CONNECTED! </font></html>");
                else
                    textL.setText("<html>Database Status :<font color='green'> CONNECTED! </font></html>");
        
                textL.setFont(new Font("Arial", Font.PLAIN, 18));
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
        });
        
        force.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
                if(JOptionPane.showConfirmDialog(null, "Are you sure you want to close? All progress will be lost!", 
                        "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                  System.exit(0);
                }
        });
        
        all.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
                if(JOptionPane.showConfirmDialog(null, "Set 'True' to all contacts?", "Update Contacted", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    int row=model.getRowCount();
                    for(int x=0;x<row;x++){
                        jTable1.getModel().setValueAt(true,x,0);
                    }
                }
            }
        });
        
        print.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
                try{
                    writeExcel();
                }catch(WriteException write){
                    write.printStackTrace();
                }
                JOptionPane.showMessageDialog(null,"Excel file created on desktop..");
            }
        });
        
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getClickCount()== 1) {
                Point p = e.getPoint();
                int row = jTable1.rowAtPoint(p);
                int col = jTable1.columnAtPoint(p);
                Object value = jTable1.getValueAt(row, col);
                StringSelection stringSelection = new StringSelection(value.toString());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, TheProject.this);
        }
    }
});
        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
        //make the swing componets, sets title, locks the columns of the table, and adds a sorter to each column
        initComponents();
        setTitle("Contact Info");
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setRowSorter(new TableRowSorter(model));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        model.removeRow(0);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    //Swing declarations...
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1=new javax.swing.JButton();


        jToolBar1.setRollover(true);


        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );


        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(98, 230, 255));
        setBounds(new java.awt.Rectangle(20, 300, 50, 50));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        setForeground(new java.awt.Color(51, 153, 255));
        setMaximumSize(new java.awt.Dimension(720, 480));
        setMinimumSize(new java.awt.Dimension(720, 480));
        
        
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("images/yashi.png"))); // NOI18N


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(584, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pack();//pack,reset,and disable the jButton1 because i was too lazy to go through netbeans componet to remove it..
        jLayeredPane1.revalidate();
        jButton1.setVisible(false);
        jButton1.setEnabled(false);   
    }                    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TheProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TheProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TheProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TheProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //Create the window
        TheProject proj = new TheProject();
        proj.setResizable(false);
        proj.setBackground(new java.awt.Color(98, 230, 255));
        proj.setVisible(true);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Method to retrieve the data from the database
    public ArrayList<TheData> getData(){
        thedata= new ArrayList<TheData>();
        Statement stmt=null;
        try 
        {
            TheConnection tc = new TheConnection();
            Connection con = tc.getConnection();
            
            if(con!=null){
                //textL.setText("<html>Database Status :<font color='green'> CONNECTED! </font></html>");
                stmt = con.createStatement();
                
                try (ResultSet rs = stmt.executeQuery("SELECT * from contact_info WHERE contacted_yet = 'False' LIMIT 50;")) {
                    while (rs.next())
                    {
                        String title=rs.getString("title");
                        String email=rs.getString("email");
                        boolean contacted=rs.getBoolean("contacted_yet");
                        int id=rs.getInt("id");
                        long installs=rs.getLong("installs");
                        thedata.add(new TheData(contacted,title,email,id,installs));
                    }
                }
                stmt.close();
                con.close();
            } 
                else{
                    //textL.setText("<html>Database Status :<font color='red'> NOT CONNECTED! </font></html>");
                    JOptionPane.showMessageDialog(null,"Unable to connect to the data base.. Please try again, and if the problem persists see technical help."
                            + "", "Oops!" , JOptionPane.WARNING_MESSAGE);
                    return null;
                    
            }
        }
        catch (SQLException | HeadlessException e) 
        {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return thedata;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////Method  to Update data to the database
    public boolean UpdateD(){
        Statement stmt=null;
        Connection c = null;
        try 
        {
            TheConnection tc = new TheConnection();
            Connection con = tc.getConnection();
            
            if(con!=null){
            //textL.setText("<html>Database Status :<font color='green'> CONNECTED! </font></html>");
            int x=jTable1.getRowCount();
            for(int n=0; n<x; n++){
                boolean hold=(boolean)jTable1.getValueAt(n,0);
                if(hold){
                    stmt = con.createStatement();
                    String sql = "UPDATE contact_info set contacted_yet ='True' where id=" + "'" +String.valueOf((int)jTable1.getValueAt(n,3)) + "'";
                    stmt.executeUpdate(sql);
                    stmt.close();
                }
                
                if(!hold){
                    stmt = con.createStatement();
                    String sql = "UPDATE contact_info set contacted_yet ='False' where id=" + "'" +String.valueOf((int)jTable1.getValueAt(n,3)) + "'";
                    stmt.executeUpdate(sql);
                    stmt.close();
                }
            }
            con.close();
            return true;
            }          
            else{
                //textL.setText("<html>Database Status :<font color='red'> NOT CONNECTED! </font></html>");
                JOptionPane.showMessageDialog(null,"Unable to connect to the data base.. Please try again, and if the problem persists see technical help."
                            + "", "Oops!" , JOptionPane.WARNING_MESSAGE);
                return false;
            }       
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+ e.getMessage());
            System.exit(0);
            return false;
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////writes the data to excel file
    public void writeExcel() throws WriteException{
        JOptionPane.showMessageDialog(null, "Please close the excel file if already opened to see changes!", "Close Contacted Info", JOptionPane.WARNING_MESSAGE);
        try{
            ww = Workbook.createWorkbook(new File(System.getProperty("user.home") + "\\Desktop\\Contact Info Sheet.xls"));
            sheet = ww.createSheet("Testing", 0);
            Label testing = new Label(0,10,"HIII");
            for(int a=0;a<=jTable1.getRowCount();a++){
                label = new Label(a, 0, String.valueOf(jTable1.getValueAt(0,a)));
                label1 = new Label(a, 1, String.valueOf(jTable1.getValueAt(1,a)));
                label2 = new Label(a, 2, String.valueOf(jTable1.getValueAt(2,a)));
                label3=new Label(a, 3, String.valueOf(jTable1.getValueAt(3,a)));
                sheet.addCell(label);
                sheet.addCell(label1);
                sheet.addCell(label2);
                sheet.addCell(label3);
            }
            ww.write();
            ww.close();
        }catch(IOException exc){
            exc.printStackTrace();
        }
        
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void windowClosing(WindowEvent e) {//logic for windowlistening        
        if(saved) System.exit(0);
        JOptionPane.showMessageDialog(null, "Please push the data before closing!", "Exit", JOptionPane.WARNING_MESSAGE);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//useless methods neeed for windowlistening implementation..
        public void windowClosed(WindowEvent e) { }
        public void windowOpened(WindowEvent e) { }
        public void windowIconified(WindowEvent e) { }
        public void windowDeiconified(WindowEvent e) { }
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) { }
        public void windowGainedFocus(WindowEvent e) {}
        public void windowLostFocus(WindowEvent e) { }
        public void windowStateChanged(WindowEvent e) { }
        public void lostOwnership(Clipboard clipboard, Transferable contents) {}
        
}//end of prog..