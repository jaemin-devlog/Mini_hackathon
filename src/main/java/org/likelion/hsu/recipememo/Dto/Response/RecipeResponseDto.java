package org.likelion.hsu.recipememo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.likelion.hsu.recipememo.Entity.Recipe;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponseDto {
    private Long id;
    private String title;
    private String category;
    private String content;
    private List<String> steps;
    private String imageUrl;

    public static RecipeResponseDto from(Recipe recipe) {
        return new RecipeResponseDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getCategory(),
                recipe.getContent(),
                recipe.getSteps(),
                recipe.getImageUrl()
        );
    }
}
