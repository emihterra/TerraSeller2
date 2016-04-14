/**
 * Created by emih on 14.04.2016.
 */

(function () {
    'use strict';

    angular
        .module('app.terraSeller', [])
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {

        $stateProvider
            .state('app.terraSeller', {
                abstract: true,
                data: {
                    title: ''
                }
            })

            .state('app.terraSeller.search', {
                url: '/seller-search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Поиск продукта',
                    title: 'Поиск продукта'
                },
                views: {
                    "content@app": {
                        templateUrl: 'app/TerraSeller/search/search.html',
                        controller: 'terraSellerSearchController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
