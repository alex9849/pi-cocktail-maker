<template>
  <q-page padding>
    <q-breadcrumbs>
      <q-breadcrumbs-el label="Public recipes" :to="{name: 'publicrecipes'}"/>
      <q-breadcrumbs-el label="Recipe details"/>
    </q-breadcrumbs>
    <div class="row innerpadding" style="display: inline">
      <div class="col vcenter">
        <h5>{{ recipe.name }}</h5>
      </div>
    </div>
    <div class="q-col-gutter-md">
      <TopButtonArranger>
        <q-btn
          color="grey"
          :to="{name: 'recipeedit', params: {id: $route.params.id}}"
          v-if="loaded && (isAdminRole || (recipe.owner && user.id === recipe.owner.id && isRecipeCreatorRole))"
        >
          Edit
        </q-btn>
        <q-btn
          v-if="!loaded || doPumpsHaveAllIngredients(recipe)"
          color="positive"
            @click="showMakeCocktailDialog = true"
            :loading="!loaded"
          >
            Make cocktail
          </q-btn>
          <q-btn
            v-else-if="areEnoughPumpsAvailable(recipe)"
            color="warning"
            @click="showMakeCocktailDialog = true"
            :disable="!isPumpIngredientEditorRole"
          >
            Change pumplayout & make cocktail
            <q-tooltip
              v-if="!isPumpIngredientEditorRole">
              You are not permitted to change the pumplayout!
            </q-tooltip>
          </q-btn>
          <q-btn
            v-else
            color="positive"
            disable
          >
            Make cocktail
            <q-tooltip>
              Not enough pumps installed!
            </q-tooltip>
          </q-btn>
        <q-btn
          color="red"
          @click.native="deleteDialog = true"
          :loading="deleting"
          v-if="loaded && (isAdminRole || (user.id === recipe.owner.id && isRecipeCreatorRole))"
        >
          Delete
        </q-btn>
      </TopButtonArranger>
      <div class="row q-col-gutter-md">
        <div class="col-12 col-sm-6">
          <div>
            <q-img
              :src="(!!recipe.id)? ($store.getters['auth/getFormattedServerAddress'] + '/api/recipe/' + recipe.id + '/image?timestamp=' + recipe.lastUpdate.getMilliseconds()):null"
              placeholder-src="../assets/cocktail-solid.png"
              :ratio="16/9"
              class="rounded-borders shadow-1"
            />
          </div>
        </div>
        <div class="col-12 col-sm-6">
          <div>
            <ingredient-list
              big
              :row1-color="'#f3f3fa'"
              :row2-color="'#fafafa'"
              class="bg-grey-3 shadow-2"
              v-model="recipe.recipeIngredients"
            />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col">
          <q-card  bordered class="bg-grey-3 shadow-1" style="min-height: 100px">
            <q-card-section>
              <b>Description:</b>
              <div style="min-width: 200px; white-space: pre-line" class="col">
                {{ recipe.description }}
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
    <c-question
      v-model="deleteDialog"
      :loading="deleting"
      ok-color="red"
      ok-button-text="Delete"
      question="Are you sure, that you want to delete this recipe?"
      @clickOk="deleteRecipe"
      @clickAbort="deleteDialog = false"
    />
    <c-make-cocktail-dialog
      v-if="loaded"
      v-model="showMakeCocktailDialog"
      :recipe="recipe"
    />
  </q-page>
</template>

<script>
import RecipeService from '../services/recipe.service'
import IngredientList from "../components/IngredientList";
import CQuestion from "../components/CQuestion";
import {mapGetters} from "vuex";
import CMakeCocktailDialog from "../components/CMakeCocktailDialog";
import TopButtonArranger from "components/TopButtonArranger";

export default {
  name: "RecipeDetails",
  components: {TopButtonArranger, CMakeCocktailDialog, CQuestion, IngredientList},
  data() {
    return {
      recipe: {},
      deleting: false,
      deleteDialog: false,
      loaded: false,
      showMakeCocktailDialog: false
    }
  },
  created() {
      this.initialize();
    },
    methods: {
      initialize() {
        RecipeService.getRecipe(this.$route.params.id)
          .then(recipe => {
            this.recipe = recipe;
            this.loaded = true;
          });
      },
      deleteRecipe() {
        this.deleting = true;
        RecipeService.deleteRecipe(this.recipe)
          .then(() => {
            this.$router.push({name: 'publicrecipes'});
            this.deleting = false;
            this.$q.notify({
              type: 'positive',
              message: 'Recipe deleted successfully'
            });
          })
      }
    },
    watch: {
      '$route.params.id': function () {
        this.initialize();
      }
    },
    computed: {
      ...mapGetters({
        user: 'auth/getUser',
        isAdminRole: 'auth/isAdmin',
        isRecipeCreatorRole: 'auth/isRecipeCreator',
        isPumpIngredientEditorRole: 'auth/isPumpIngredientEditor',
        doPumpsHaveAllIngredients: 'pumpLayout/doPumpsHaveAllIngredientsForRecipe',
        areEnoughPumpsAvailable: 'pumpLayout/areEnoughPumpsAvailable'
      })
    }
  }
</script>

<style scoped>
  .innerpadding > * {
    padding: 10px;
  }

  .vcenter {
    display: inline-block;
    vertical-align: middle;
    float: none;
  }
</style>
