import axios from 'axios';

const API_PATH = 'api/recipe/';

class RecipeService {
  createRecipe(recipe, image) {
    let uploadData = new FormData();
    const stringRecipe = JSON.stringify(recipe);
    const blobRecipe = new Blob([stringRecipe], {
      type: 'application/json'
    });
    uploadData.append("recipe", blobRecipe);
    if(image) {
      uploadData.append("image", image);
    }
    return axios.post(API_PATH, uploadData, {headers:{'Content-Type' :'multipart/form-data'}})
      .then(response => response.data);
  }

  getRecipes(page, ownerId, inPublic, searchName) {
    let path = API_PATH + "?page=" + page;
    if(ownerId != null) {
      path += "&ownerId=" + ownerId;
    }
    if(inPublic != null) {
      path += "&inPublic=" + inPublic;
    }
    if(searchName != null) {
      path += "&searchName=" + searchName;
    }
    return axios.get(path)
      .then(response => response.data);
  }

  getRecipe(id) {
    return axios.get(API_PATH + id)
      .then(response => response.data);
  }

  updateRecipe(recipe, image, removeImage) {
    let uploadData = new FormData();
    const stringRecipe = JSON.stringify(recipe);
    const blobRecipe = new Blob([stringRecipe], {
      type: 'application/json'
    });
    uploadData.append("recipe", blobRecipe);
    if(image) {
      uploadData.append("image", image);
    }
    return axios.put(API_PATH + recipe.id + "?removeImage=" + !!removeImage, uploadData, {headers:{'Content-Type' :'multipart/form-data'}});
  }

  deleteRecipe(recipe) {
    return axios.delete(API_PATH + recipe.id);
  }
}

export default new RecipeService();
