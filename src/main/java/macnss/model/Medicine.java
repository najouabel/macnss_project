package macnss.model;
public class Medicine extends Document  {

    private Double borCode;
    private String nom;
    private String percentage;
    private double price;

    public Medicine(Double barcode) {
        this.borCode = barcode;
    }

    public String getNom(){
        return this.nom;
    }

    public Double getBorCode(){
        return this.borCode;
    }

    public double getPrice(){
        return this.price;
    }

    public String getPercentage() {
        return this.percentage;
    }


    public void setNom(String nom){
        this.nom = nom;
    }

    public void setBorCode(Double barCode){
        this.borCode = barCode;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
    public double getPercentageAsDouble() {
        if (percentage != null && percentage.endsWith("%")) {
            String percentageWithoutPercentSign = percentage.substring(0, percentage.length() - 1);
            try {
                return Double.parseDouble(percentageWithoutPercentSign);
            } catch (NumberFormatException e) {
                // Handle the parsing error, e.g., return a default value or throw an exception
                return 0.0; // Default value in case of parsing error
            }
        } else {
            // Handle the case where the percentage doesn't end with "%"
            return 0.0; // Default value for such cases
        }
    }

}
