<template>
  <div>
    <c-ingredient-selector
      v-model="value.ingredient"
      clearable
      @input="() => {$emit('input', value); $v.value.ingredient.$touch();}"
      :rules="[val => $v.value.ingredient.required || 'Required']"
    />
    <q-input
      label="Amount (in ml)"
      type="number"
      outlined
      v-model.number="value.amount"
      @input="() => {$emit('input', value); $v.value.amount.$touch();}"
      :rules="[
        val => $v.value.amount.required || 'Required',
        val => $v.value.amount.minValue || 'Min 1ml'
      ]"
    />
    <slot name="below"/>
  </div>
</template>

<script>
import IngridientService from '../services/ingredient.service'
import {minValue, required} from "vuelidate/lib/validators";
import CIngredientSelector from "./CIngredientSelector";

export default {
    name: "IngredientForm",
    components: {CIngredientSelector},
    props: {
      value: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        ingredientOptions: []
      }
    },
    methods: {
      filterIngredients(val, update, abort) {
        if(val.length < 1) {
          abort();
          return;
        }
        IngridientService.getIngredientsFilter(val)
          .then(ingridients => {
            update(() => {
              this.ingredientOptions = ingridients;
            })
          }, () => abort)
      },
      abortFilterIngredients() {
      },
      initialize() {
        this.$v.value.$touch();
        if(this.$v.value.$invalid) {
          this.$emit('invalid');
        } else {
          this.$emit('valid');
        }
      }
    },
    created() {
      this.initialize();
    },
    validations() {
      let validations = {
        value: {
          ingredient: {
            required
          },
          amount: {
            required,
            minValue: minValue(1)
          }
        }
      };
      return validations;
    },
    watch: {
      '$v.value.$invalid': function _watch$vValue$invalid(value) {
        if (!value) {
          this.$emit('valid');
        } else {
          this.$emit('invalid');
        }
      },
      value() {
        this.initialize();
      }
    }
  }
</script>

<style scoped>
</style>
