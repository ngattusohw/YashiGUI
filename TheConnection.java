import java.sql.DriverManager;


public class TheConnection extends Thread{
    
    private volatile java.sql.Connection conn = null;
    private boolean sleep = true;
        @Override
        public void run() {
            try {
                String driver = "org.postgresql.Driver";
                Class.forName(driver).newInstance();                       
                //timeout
                DriverManager.setLoginTimeout(10);
                this.conn = DriverManager.getConnection("jdbc:postgresql://52.20.130.81/","postgres","");
                sleep = false;
                 } catch (Exception e) {}
             }
                
    public java.sql.Connection getConnection() {
        TheConnection d = new TheConnection() ;
        d.start() ;
        try {
            for(int i=1; i<=10; i++) {
                //Wait 1 second
                if (d.sleep){
                    Thread.sleep(250);
                }
            }  
        }catch (InterruptedException e) {
                e.printStackTrace();
            }
       return d.conn;
    }
}