appCtrllers.controller('profileCtrl', function ($scope, $window, $state, $cookieStore) {
    // Set user details
    $scope.user = $cookieStore.get('userInfo');

    // Logout user
    $scope.logout = function () {
        $cookieStore.remove("userInfo");
        $state.go('login');
        $window.location.reload();
    };
});
