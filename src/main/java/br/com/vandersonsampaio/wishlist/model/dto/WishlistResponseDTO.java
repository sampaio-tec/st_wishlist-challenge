package br.com.vandersonsampaio.wishlist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WishlistResponseDTO {

    private String userId;

    private List<ProductResponseDTO> products;
}
