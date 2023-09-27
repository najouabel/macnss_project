package macnss.model;
public class Medicine extends Document  {

    private long borCode;
    public Medicine(int id, String name, double amountPaid, Double percentage,String type,long barcode) {
        super(id, name, amountPaid, percentage, type);
        this.borCode = barcode;
    }

    public long getBorCode(){
        return this.borCode;
    }
    public void setBorCode(long barCode){
        this.borCode = barCode;
    }

}
