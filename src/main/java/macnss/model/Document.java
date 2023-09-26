package macnss.model;

public abstract class Document {
    int id;
    String name;
    double amountPaid;
    String percentage;

    public Document(int id, String name, double amountPaid, String percentage) {
        this.id = id;
        this.name = name;
        this.amountPaid = amountPaid;
        this.percentage = percentage;
    }

    public Document() {

    }

    //public double CalculateRefundedAmount() {
       // return amountPaid * percentage / 100;
    //}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
