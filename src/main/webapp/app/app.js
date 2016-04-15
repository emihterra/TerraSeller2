/**
 * @ngdoc overview
 * @name app [smartadminApp]
 * @description
 * # app [smartadminApp]
 *
 * Main module of the application.
 */

(function() {
    'use strict';

angular.module('app', [
    'ngSanitize',
    'ngAnimate',
    'restangular',
    'ui.router',
    'ui.bootstrap',

    // Smartadmin Angular Common Module
    'SmartAdmin',

    // App
    'app.jHipster',
    'app.terraSeller',
    'app.layout',
    //'app.chat',
    //'app.dashboard',
    //'app.calendar',
    //'app.inbox',
    //'app.graphs',
    //'app.tables',
    //'app.forms',
    'app.ui',
    //'app.widgets',
    //'app.maps',
    //'app.appViews',
    //'app.misc',
    //'app.smartAdmin',
    //'app.eCommerce'
    'app.home'
])
.config(function ($provide, $httpProvider, RestangularProvider) {


    // Intercept http calls.
    $provide.factory('ErrorHttpInterceptor', function ($q) {
        var errorCounter = 0;
        function notifyError(rejection){
            $.bigBox({
                title: rejection.status + ' ' + rejection.statusText,
                content: rejection.data,
                color: "#C46A69",
                icon: "fa fa-warning shake animated",
                number: ++errorCounter,
                timeout: 6000
            });
        }

        return {
            // On request failure
            requestError: function (rejection) {
                // show notification
                notifyError(rejection);

                // Return the promise rejection.
                return $q.reject(rejection);
            },

            // On response failure
            responseError: function (rejection) {
                // show notification
                notifyError(rejection);
                // Return the promise rejection.
                return $q.reject(rejection);
            }
        };
    });

    // Add the interceptor to the $httpProvider.
    $httpProvider.interceptors.push('ErrorHttpInterceptor');

    RestangularProvider.setBaseUrl(location.pathname.replace(/[^\/]+?$/, ''));

})
.constant('APP_CONFIG', window.appConfig)

.run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        //$rootScope.$state = $state;
        //$rootScope.$stateParams = $stateParams;
        stateHandler.initialize();
        translationHandler.initialize();
    }

})();

