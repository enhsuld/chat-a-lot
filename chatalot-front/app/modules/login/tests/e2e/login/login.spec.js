var HomePage = require('./login.po.js');

describe('Home page tests',function(){
    var homePage = new HomePage();

    /**
     * Before each function, load the default landing page of the app
     */
    beforeEach(function() {
      browser.get('http://127.0.0.1:9000/');
    });

    /**
     * Should navigate to the login page when application loads
     */
    it('should navigate to Home page by default', function(){
        expect(browser.getCurrentUrl()).toEqual('http://127.0.0.1:9000/#!/');
    });

    /**
     * Should containg correct app heading text
     */
    it('should contain correct heading text', function(){
        homePage.goToHome();
        expect(homePage.getAppHeading()).toBe('AngularJs Cordova application');
    });

    /**
     * Should containg correct login page heading text
     */
    it('should contain correct login page heading text', function(){
        homePage.goToHome();
        expect(homePage.getHomeHeading()).toBe('Congratulations!');
    });

    /**
     * Should containg correct login page content text
     */
    it('should containg correct login page content text', function(){
        homePage.goToHome();
        expect(homePage.getHomeContent()).toBe('You have successfully created an AngularJs application running within a cordova app !');
    });

});
