import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Recipe } from '../object/recipe';
import { RecipeService } from '../recipe.service';

@Component({
  selector: 'app-recipe-list',
  templateUrl: './recipe-list.component.html',
  styleUrls: ['./recipe-list.component.css']
})
export class RecipeListComponent implements OnInit {

  recipes: Recipe[];

  editState: boolean;
  selectedRecipe: Recipe;
  
  constructor(private recipeService: RecipeService ) {}

  ngOnInit() {
    this.getRecipes();
  }

  getRecipes(): void {
    console.log("getRecipes()");
    this.recipeService.getRecipes().subscribe(
      recipes => this.recipes = recipes
    )

    this.selectedRecipe = undefined;
    this.editState = false;
  }
  openDetailView(recipe: Recipe): void {
    console.log("View recipe "+recipe.title+"");
     this.recipeService.getImage(recipe.id).subscribe(
       photo => recipe.photo = photo.toString()
    );
    this.selectedRecipe = recipe;
    this.editState = false;
  }
  openEditView(recipe: Recipe): void {
    console.log("Edit recipe "+recipe.title+"");
    this.selectedRecipe = recipe;
    this.editState = true;
  }

  closeDetailView(){
    console.log('Event Emmiter closeDetailView ');
    this.selectedRecipe = undefined;
  }

  add(title: string): void {
    title = title.trim();
    if (!title) return;
    console.log("New recipe: "+title+"");
    var newRecipe = { title } as Recipe;
    this.recipeService.addRecipe(newRecipe).subscribe(
      recipe => {
        console.log("Create recipe ok");
        this.getRecipes();
        this.openEditView(recipe);
      }
    )
  }

  delete(recipe: Recipe): void {
    console.log("delete recipe: "+recipe.title+"");
    this.recipes = this.recipes.filter(r => r !== recipe);
    this.recipeService.deleteRecipe(recipe).subscribe();
  }

  

}
