import { Component, Input, Output, EventEmitter, OnChanges, ViewChild } from '@angular/core';
import { Recipe } from '../object/recipe';
import { FormGroup, FormBuilder, FormArray, FormControl } from '@angular/forms';
import { RecipeService } from '../recipe.service';
import { isDefined } from '@angular/compiler/src/util';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.css']
})
export class RecipeDetailComponent implements OnChanges {
  @Input() recipe: Recipe;
  @Input() editState: boolean;

  @Output('close') change = new EventEmitter();

  recipeForm: FormGroup;

  @ViewChild("photo") photo;

  categories = [
    'Entrée',
    'Plat',
    'Dessert',
    'Boisson'
  ];

  constructor(
    private fb: FormBuilder,
    private recipeService: RecipeService
  ) {
    this.createForm();
  }

  createForm() {
    this.recipeForm = this.fb.group({
      title: '',
      nbPers: '',
      making: '',
      baking: '',
      cooling: '',
      makeDiff: '',
      toTest: '',
      foundOn: '',
      categ: '',
      ingredients: [],
      steps: [],
      // displayPhoto: File
    });
  }

  ngOnChanges(): void {
    this.rebuildForm();
  }

  rebuildForm() {
    this.recipeForm.reset({
      title: this.recipe.title,
      nbPers: this.recipe.nbPers,
      making: this.recipe.making,
      baking: this.recipe.baking,
      cooling: this.recipe.cooling,
      makeDiff: this.recipe.makeDiff,
      toTest: this.recipe.toTest,
      foundOn: this.recipe.foundOn,
      categ: this.recipe.categ
    });
    this.setIngredients(this.recipe.ingredients);
    this.setSteps(this.recipe.steps);
    // this.recipeService.downloadFile(this.recipe.id).subscribe(
    //   file => this.recipeForm.setControl('displayPhoto', file)
    // );
  }
  reset() {
    this.rebuildForm();
    this.editState = true;

  }

  setIngredients(ingredients: string[]) {
    if (!ingredients) {
      ingredients = [];
    }
    const ingredientsFormArray = this.fb.array(ingredients);
    this.recipeForm.setControl('ingredients', ingredientsFormArray);
  }

  setSteps(steps: string[]) {
    if (!steps) {
      steps = [];
    }
    const stepsFormArray = this.fb.array(steps);
    this.recipeForm.setControl('steps', stepsFormArray);
  }

  getPhoto(){
    return this.recipeService.getImage(this.recipe.id).subscribe(
      // photo => photo
    );
  }


  get ingredients(): FormArray {
    console.log("--------------- ingredients list get");
    return this.recipeForm.get('ingredients') as FormArray;
  };

  get steps(): FormArray {
    console.log("--------------- steps list get");
    return this.recipeForm.get('steps') as FormArray;
  };

  addIngredient(ingredient) {
    console.log('add ingredient in list ' + ingredient.value);
    this.ingredients.push(new FormControl(ingredient.value));

    ingredient.value = '';
    ingredient.focus()
  }

  delIngredient(index) {
    console.log('Remove ingredient in list ' + this.ingredients[index]);
    this.ingredients.removeAt(index);
  }

  addStep(step) {
    console.log('add step in list ' + step.value);
    this.steps.push(new FormControl(step.value));
    step.value = '';
    step.focus()
  }

  delStep(index) {
    console.log('Remove step in list ' + this.steps[index]);
    this.steps.removeAt(index);
  }
  prepareSaveRecipe(): Recipe {
    const formModel = this.recipeForm.value;
    const saveRecipe: Recipe = {
      id: this.recipe.id,
      title: formModel.title as string,
      nbPers: formModel.nbPers as string,
      making: formModel.making as string,
      baking: formModel.baking as string,
      cooling: formModel.cooling as string,
      makeDiff: formModel.makeDiff as string,
      toTest: formModel.toTest as string,
      foundOn: formModel.foundOn as string,
      categ: formModel.categ as string,
      ingredients: formModel.ingredients as Array<string>,
      steps: formModel.steps as Array<string>,
      photo: null
    }
    return saveRecipe;
  }

  onSubmit(): void {
    this.recipe = this.prepareSaveRecipe();
    this.recipeService.updateRecipe(this.recipe).subscribe(
      recipe => {
        console.log("Update recipe " + recipe.title + " done");

        // const formModel = this.recipeForm.value;
        // console.log(formModel);
        // Retrieve File to upload

        let fi = this.photo.nativeElement;
        if (fi.files && fi.files[0]) {
          let fileToUpload = fi.files[0];

          console.log("Upload photo for recipe " + recipe.id);
          this.recipeService.uploadFile(recipe.id, fileToUpload).subscribe(
            any => console.log("upload done")
          )
        }
        // Then
        this.goList()
      }
    );
  }

  // getPhoto(){
  //  return this.recipeService.downloadFile(this.recipe.id).subscribe(
  //       // file => return file
  //     );
  // }


  displayForm(): boolean {
    return this.editState;
  }

  goList(): void {
    this.change.emit();
  }

  goDetails(): void {
    this.editState = false;

  }

  edit(recipe: Recipe): void {
    console.log("Edit recipe " + recipe.title + "");
    this.editState = true;
  }
}
