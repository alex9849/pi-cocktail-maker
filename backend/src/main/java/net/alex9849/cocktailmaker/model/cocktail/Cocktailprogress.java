package net.alex9849.cocktailmaker.model.cocktail;

import net.alex9849.cocktailmaker.model.recipe.Recipe;
import net.alex9849.cocktailmaker.model.user.User;

public class Cocktailprogress {
    private Recipe recipe;
    private int progress;
    private User user;
    private boolean isCanceled;
    private boolean isDone;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }
}
