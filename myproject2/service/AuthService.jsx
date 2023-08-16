import axios from "axios";

const url = "http://localhost:8080/jip/project/login";
const AuthService = (function(){
    const _signin = async (credentials) => {
        try {
            const response = await axios.post(
                url, 
                new URLSearchParams({
                    username: credentials.username, //gave the values directly for testing
                    password: credentials.password,
                  }),
                 {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },}
            );
            if(response.status === 200){
                return true; 
            }
        } catch (error) {
            console.log(error);
        }
        return false; 
    };
    const _logout = async () => {
        try {
          const response = await axios.post('http://localhost:8080/jip/project/logout'); 
          if (response.status === 200) {
            return true;
          }
        } catch (error) {
          console.log(error);
        }
        return false;
      };
    return {
        signin: _signin,
        logout: _logout,
    };
    
})();


export default AuthService;