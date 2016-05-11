(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-room', {
            parent: 'entity',
            url: '/client-room',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.terraSeller.clientRoom.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-room/client-rooms.html',
                    controller: 'ClientRoomController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientRoom');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('client-room-detail', {
            parent: 'entity',
            url: '/client-room/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.terraSeller.clientRoom.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-room/client-room-detail.html',
                    controller: 'ClientRoomDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientRoom');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClientRoom', function($stateParams, ClientRoom) {
                    return ClientRoom.get({id : $stateParams.id});
                }]
            }
        })
        .state('client-room.new', {
            parent: 'client-room',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-room/client-room-dialog.html',
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
                    $state.go('client-room', null, { reload: true });
                }, function() {
                    $state.go('client-room');
                });
            }]
        })
        .state('client-room.edit', {
            parent: 'client-room',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-room/client-room-dialog.html',
                    controller: 'ClientRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientRoom', function(ClientRoom) {
                            return ClientRoom.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-room', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-room.delete', {
            parent: 'client-room',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-room/client-room-delete-dialog.html',
                    controller: 'ClientRoomDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientRoom', function(ClientRoom) {
                            return ClientRoom.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-room', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
