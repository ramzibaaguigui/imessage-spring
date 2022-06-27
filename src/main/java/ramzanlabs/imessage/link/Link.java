package ramzanlabs.imessage.link;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Link {

    @Column(name = "id", unique = true)
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "original_link")
    @JsonProperty("original_link")
    private String original;

    @Column(name = "shortened_link", unique = true)
    @JsonProperty("shortened_link")
    private String shortened;

    public Link() {

    }

    public Link(String original, String shortened) {

    }

    public String getOriginal() {
        return this.original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getShortened(){
        return this.shortened;
    }


    public void setShortened(String shortened) {
        this.shortened = shortened;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
