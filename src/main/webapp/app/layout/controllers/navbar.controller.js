(function() {
    'use strict';

    angular
        .module('app')
        .controller('HeaderLayoutController', HeaderLayoutController);

    HeaderLayoutController.$inject = ['$scope', '$location', '$state', '$stateParams', 'Auth', 'Principal', 'ENV', 'LoginService'];

    function HeaderLayoutController ($scope, $location, $state, $stateParams, Auth, Principal, ENV, LoginService) {
        var vm = this;

        vm.navCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.inProduction = ENV === 'prod';
        vm.login = login;
        vm.logout = logout;
        vm.$state = $state;
        vm.settingsAccount = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {

            if(account) {
                return {
                    activated: account.activated,
                    email: account.email,
                    firstName: account.firstName,
                    langKey: account.langKey,
                    lastName: account.lastName,
                    login: account.login,
                    emplcode: account.emplcode,
                    dimension: account.dimension
                };
            } else {
                return null;
            }
        };

        function loadAccountSettings() {
            Principal.identity().then(function (account) {
                vm.settingsAccount = copyAccount(account);
            });
        };

        loadAccountSettings();

        function login () {
            LoginService.open();
        }

        function logout () {
            Auth.logout();
            LoginService.open();
            //$state.go('app.home', $stateParams, {reload: true, inherit: false});
        }

        $scope.$on('authenticationSuccess', function(){
            loadAccountSettings();
        });

    }
})();
