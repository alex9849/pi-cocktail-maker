<template>
  <div id="q-app">
    <router-view/>
  </div>
</template>

<script>
  import {mapActions, mapGetters} from "vuex";

  const extendTokenBeforeExpirationInMs = 600000;

  export default {
    name: 'App',
    data() {
      return {
        tokenupdater: null
      }
    },
    created() {
      this.tokenupdater = setInterval(() => {
        if(this.getAuthToken) {
          const tokenExpiration = this.getAuthToken.tokenExpiration;
          const token = this.getAuthToken.accessToken;
          const currentDate = new Date();
          let refreshTime = new Date();
          refreshTime.setTime(tokenExpiration.getTime() - extendTokenBeforeExpirationInMs);
          if(refreshTime < currentDate) {
            this.refreshToken();
          }
        }
      }, extendTokenBeforeExpirationInMs / 3);
      if(this.getAuthToken) {
        const tokenExpiration = this.getAuthToken.tokenExpiration;
        const currentDate = new Date();
        if(tokenExpiration > currentDate) {
          this.refreshToken();
        }
      }
    },
    beforeDestroy() {
      clearInterval(this.tokenupdater);
    },
    methods: {
      ...mapActions({
        refreshToken : 'auth/refreshToken'
      })
    },
    computed: {
      ...mapGetters('auth', ['isLoggedIn', 'getAuthToken'])
    }
  }
</script>
