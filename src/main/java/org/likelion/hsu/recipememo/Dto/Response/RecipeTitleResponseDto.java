package org.likelion.hsu.recipememo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.likelion.hsu.recipememo.Entity.Recipe;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeTitleDto {
    private Long id;
    private String title;
    private String imageUrl;

    public static RecipeTitleDto from(Recipe recipe) {
        return new RecipeTitleDto(recipe.getId(), recipe.getTitle(), recipe.getImageUrl());
    }
}
