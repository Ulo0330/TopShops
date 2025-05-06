package topshopspackage;

public class product {
    private String name, price, company, type, event, totalSales, salesBy7, marketValue;

    public product() {}

    //getters
    public String getName() {return name;}
    public String getPrice() {return price;}
    public String getCompany() {return company;}
    public String getType() {return type;}
    public String getEvent() {return event;}
    public String getTotalSales() {return totalSales;}
    public String getSalesBy7() {return salesBy7;}
    public String getMarketValue() {return marketValue;}

    //setters
    public void setName(String name) {this.name = name;}
    public void setPrice(String price) {this.price = price;}
    public void setCompany(String company) {this.company = company;}
    public void setType(String type) {this.type = type;}
    public void setEvent(String event) {this.event = event;}
    public void setTotalSales(String totalSales) {this.totalSales = totalSales;}
    public void setSalesBy7(String salesBy7) {this.salesBy7 = salesBy7;}
    public void setMarketValue(String marketValue) {this.marketValue = marketValue;}

    //
    public int getTotalSalesInt() {
        try {
            return Integer.parseInt(this.totalSales);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public long getMarketValueLong() {
        if (marketValue == null) return 0;
        try {
            // Remove any non-digit characters (e.g., "$", ",", spaces)
            String cleaned = marketValue.replaceAll("[^\\d]", "");
            return cleaned.isEmpty() ? 0 : Long.parseLong(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}