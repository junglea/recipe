import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError, map, tap } from 'rxjs/operators';

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Recipe } from './object/recipe';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

/** 
 * This class is used to call REST API server
*/
@Injectable()
export class RecipeService {

  private baseUrl = 'http://localhost:8080';
  private recipesUrl = this.baseUrl + "/api/recipes"; // URL to web api

  constructor(
    private http: HttpClient) { }

  /* POST /api/recipes : Add a new recipe */
  addRecipe(recipe: Recipe): Observable<Recipe> {
    return this.http.post<Recipe>(this.recipesUrl, recipe, httpOptions)
      .pipe(
        tap((recipe: Recipe) => this.log(`added recipe w/ id=${recipe.id}`)),
        catchError(this.handleError<Recipe>('addRecipe'))
      );
  }

  /* GET /api/recipes : Retrieve all recipes  */
  getRecipes(): Observable<Recipe[]> {
    this.log('RecipeService: fetched recipes on ' + this.recipesUrl);
    return this.http.get<Recipe[]>(this.recipesUrl)
      .pipe(
        tap(recipes => this.log(`fetched recipes`)),
        catchError(this.handleError('getRecipes', []))
      );
  }
  /* DELETE /api/recipes/ID : Delete a recipe with ID */
  deleteRecipe(recipe: Recipe | number): Observable<Recipe> {
    const id = typeof recipe === 'number' ? recipe : recipe.id;
    const url = `${this.recipesUrl}/${id}`;

    return this.http.delete<Recipe>(url, httpOptions)
      .pipe(
        tap(_ => this.log(`deleted recipe id=${id}`)),
        catchError(this.handleError<Recipe>('deleteRecipe'))
      );
  }

  /* GET /api/recipes?title= : Retrieve recipes whose title contains search title */
  searchRecipes(term: string): Observable<Recipe[]> {
    this.log(`Call searchRecipes with "${term}" on url ` + this.recipesUrl + `?title=${term}`)
    if (!term.trim()) {
      // if not search title, return empty recipe array.
      return of([]);
    }
    // Add safe, URL encoded search parameter if there is a search term
    const options = term ?
      { params: new HttpParams().set('title', term) } : {};
    return this.http.get<Recipe[]>(this.recipesUrl, options)
      .pipe(
        tap(_ => this.log(`found recipes matching "${term}"`)),
        catchError(this.handleError<Recipe[]>('searchRecipe', []))
      );
  }

  /* GET /api/recipes/ID : Retrieve a recipe with ID */
  getRecipe(id: string): Observable<Recipe> {

    const url = `${this.recipesUrl}/${id}`;
    return this.http.get<Recipe>(url)
      .pipe(
        tap(_ => this.log(`fetched recipe id=${id}`)),
        catchError(this.handleError<Recipe>(`getRecipe id=${id}`))
      );
  }

  /* PUT  /api/recipes/ID : Update recipe with ID */
  updateRecipe(recipe: Recipe): Observable<any> {
    const id = typeof recipe === 'number' ? recipe : recipe.id;
    const url = `${this.recipesUrl}/${id}`;
    return this.http.put(url, recipe, httpOptions)
      .pipe(
        tap(_ => this.log(`updated recipe id=${recipe.id}`)),
        catchError(this.handleError<any>('updateRecipe'))
      );
  }

  uploadFile(id: string, file: File): Observable<any> {
    const url = `${this.recipesUrl}/${id}/upload`;
    this.log(`Upload file "${file.name}" on url ${url} `)
    let formData = new FormData();
    formData.append('file', file);

    let params = new HttpParams();

    const options = {
      params: params,
      reportProgress: true
    };

    return this.http.post(url, formData, options);
  }

  getImage(id: string) {
    const imageUrl = `${this.recipesUrl}/${id}/photo`;
    return this.http.get(imageUrl, {responseType: 'text'}).pipe(
      tap(_ => this.log(`download photo for recipe id=${id}`)),
      catchError(this.handleError<Recipe>(`downloadFile id=${id}`))
    );
  }



  /** 
   * 
   * Commons methods to externalise 
   * 
   * */

  /** Log a RecipeService message with the MessageService */
  private log(message: string) {
    console.log('RecipeService: ' + message);
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }


}
