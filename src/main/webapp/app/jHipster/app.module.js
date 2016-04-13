(function() {
    'use strict';

    angular
        .module('app.jHipster', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            //'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            //'ui.router',
            'infinite-scroll',
            'angular-loading-bar'
        ])
  .config(function ($stateProvider
    ) {
/*
    $stateProvider.state('authlogin', {
        url: '/auth',

        views: {
            "content@app": {
                templateUrl: "app/jHipster/components/login/authLogIn.html",
                controller: 'LoginController',
                controllerAs: 'vm'
            }
        },
        data: {
            title: 'Авторизация',
            rootId: 'extra-page'
        }

    })
*/
});

})();
