appCtrllers.controller('loginCtrl', ['$scope', '$state', '$cookies','FinApp',
function ($scope, $state, $cookies, FinApp) {

    $scope.fbLogin = function () {
        FinApp.fbLogin().then(function(user){
            $cookies.putObject('userInfo', user);
            $state.go('profile');
        })
    };

}]);
