package ir.bmi.EDA;

public class NewUserMessage extends UserMessage{
    private int age;
    private String picture;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "NewUserMessage{" +
                "name="+getName()+
                "id="+getId()+
                "email="+getEmail()+
                "age=" + age +
                ", picture='" + picture + '\'' +
                '}';
    }
}
