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
            })

            .state('app.terraSeller.client', {
                url: '/client',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Выбор клиента',
                    title: 'Выбор клиента'
                },
                views: {
                    "content@app": {
                        templateUrl: 'app/TerraSeller/client/client.html',
                        controller: 'terraSellerClientController',
                        controllerAs: 'vm'
                    }
                }
            })

            .state('app.terraSeller.client-room', {
                url: '/client-room',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Помещения',
                    title: 'Помещения'
                },
                views: {
                    'content@app': {
                        templateUrl: 'app/TerraSeller/client/ClientRoom/client-rooms.html',
                        controller: 'ClientRoomController',
                        controllerAs: 'vm'
                    }
                }
            })

            .state('app.terraSeller.client-room.new', {
                url: '/new_client_room',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Создание помещения',
                    title: 'Создание помещения'
                },
                views: {
                    'content@app': {
                        templateUrl: 'app/TerraSeller/client/ClientRoom/client-room-dialog.html',
                        controller: 'ClientRoomDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            client: 0,
                            name: null,
                            r_length: null,
                            r_width: null,
                            r_height: null,
                            bottom_border_height: null,
                            top_border_height: null,
                            id: null
                        };
                    }
                }
            })

            .state('app.terraSeller.client-room.edit', {
                parent: 'app.terraSeller.client-room',
                url: '/edit_client_room/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Создание помещения',
                    title: 'Создание помещения'
                },
                views: {
                    'content@app': {
                        templateUrl: 'app/TerraSeller/client/ClientRoom/client-room-dialog.html',
                        controller: 'ClientRoomDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ClientRoom', function($stateParams, ClientRoom) {
                        return ClientRoom.get({id : $stateParams.id});
                    }]
                }
            })

            .state('app.terraSeller.client-basket', {
                url: '/client-basket',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Корзины',
                    title: 'Корзины'
                },
                views: {
                    'content@app': {
                        templateUrl: 'app/TerraSeller/client/ClientBasket/client-baskets.html',
                        controller: 'ClientBasketController',
                        controllerAs: 'vm'
                    }
                }
            });
        
    }
})();
