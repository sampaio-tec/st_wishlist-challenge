package br.com.vandersonsampaio.wishlist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "wishlist")
public class Wishlist {

    @Id
    private String id;
    private String userId;
    private String productId;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime insertTime;

    public Wishlist(String userId, String productId) {
        this.userId = userId;
        this.productId = productId;
        insertTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

       if(obj == null || obj.getClass()!= this.getClass())
            return false;

        Wishlist wishlist = (Wishlist) obj;

        return (wishlist.userId.equals(this.userId) && wishlist.productId.equals(this.productId));
    }
}
