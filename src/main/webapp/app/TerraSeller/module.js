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
                parent: 'app.terraSeller.client-room',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/TerraSeller/client/ClientRoom/client-room-dialog.html',
                        controller: 'ClientRoomDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    client: null,
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
                    }).result.then(function() {
                        $state.go('app.terraSeller.client-room', null, { reload: true });
                    }, function() {
                        $state.go('app.terraSeller.client-room');
                    });
                }]
            });

    }
})();
