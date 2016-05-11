(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-room-item', {
            parent: 'entity',
            url: '/client-room-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.terraSeller.clientRoomItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-room-item/client-room-items.html',
                    controller: 'ClientRoomItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientRoomItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('client-room-item-detail', {
            parent: 'entity',
            url: '/client-room-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.terraSeller.clientRoomItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-room-item/client-room-item-detail.html',
                    controller: 'ClientRoomItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientRoomItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClientRoomItem', function($stateParams, ClientRoomItem) {
                    return ClientRoomItem.get({id : $stateParams.id});
                }]
            }
        })
        .state('client-room-item.new', {
            parent: 'client-room-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-room-item/client-room-item-dialog.html',
                    controller: 'ClientRoomItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id_client_room: null,
                                name: null,
                                item_type: null,
                                i_width: null,
                                i_height: null,
                                i_top: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('client-room-item', null, { reload: true });
                }, function() {
                    $state.go('client-room-item');
                });
            }]
        })
        .state('client-room-item.edit', {
            parent: 'client-room-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-room-item/client-room-item-dialog.html',
                    controller: 'ClientRoomItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientRoomItem', function(ClientRoomItem) {
                            return ClientRoomItem.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-room-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-room-item.delete', {
            parent: 'client-room-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-room-item/client-room-item-delete-dialog.html',
                    controller: 'ClientRoomItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientRoomItem', function(ClientRoomItem) {
                            return ClientRoomItem.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-room-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
