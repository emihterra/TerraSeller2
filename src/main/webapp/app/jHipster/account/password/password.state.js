(function() {
    'use strict';

    angular
        .module('app.jHipster')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('password', {
            parent: 'account',
            url: '/password',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'global.menu.account.password',
                title: 'Профиль / Пароль'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/jHipster/account/password/password.html',
                    controller: 'PasswordController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('password');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
