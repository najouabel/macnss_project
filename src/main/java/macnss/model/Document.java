package macnss.model;

public class Document {
    int id;
    String name;
    double amountPaid;
    Double percentage;
    String type;

    public Document(int id, String name, double amountPaid, Double percentage,String type) {
        this.id = id;
        this.name = name;
        this.amountPaid = amountPaid;
        this.percentage = percentage;
        this.type = type;
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

    public Double getPercentage() {
        if (percentage > 0){
            return percentage;
        }else {
            return switch (type) {
                case "radio" -> 0.3;
                case "medicalExamination" -> 0.2;
                case "medicalAnalysis" -> 0.4;
                default -> 0.0;
            };
        }
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}