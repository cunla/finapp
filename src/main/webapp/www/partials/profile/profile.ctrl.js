appCtrllers.controller('profileCtrl', function ($scope, $window, $state, $cookies, FinApp) {
    $scope.user = $cookies.getObject('userInfo');

    $scope.logout = function () {
        $cookies.remove("userInfo");
        FinApp.logout();
        $state.go('login');
        $window.location.reload();
    };
});
