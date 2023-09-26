package macnss;

public class Main {
    public static void main(String[] args) {
    try {
        macnss.service.MaCNSSService MaCNSSService = macnss.service.MaCNSSService.getInstance();
        MaCNSSService.start();
    }catch (Exception e) {
        e.printStackTrace();
    }
}
}