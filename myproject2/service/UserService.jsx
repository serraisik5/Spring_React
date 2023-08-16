import axios from "axios";
const url = "http://localhost:8080/jip/project/users"

const UserService = (function () {

    const _getUserByUsername = async (username) => {
        try {
            const response = await axios.get(`${url}?name=${username}`);
            if (response && response.data) {
                return response.data;
            }
            console.log("Error fetching user by username");
        } catch (error) {
            console.error("An error occurred while fetching the user:", error);
        }
        return null; 
    };
    const _getUserById = async (userId) => {
        try {
            const response = await axios.get(`${url}/${userId}`);
            if (response && response.data) {
                return response.data;
            }
            console.log("Error fetching user by ID");
        } catch (error) {
            console.error("An error occurred while fetching the user:", error);
        }
        return null;
    };
    const _getUserWishlist = async (username) => {
        console.log("get user wish list")
        const response = await axios.get(`${url}/wishlist`, {
          params: {
            username: username
          }
        });
        console.log(response.data)
        return response.data;
    };
      const _getUserCatchlist = async (username) => {
        const response = await axios.get(`${url}/catchlist`, {
          params: {
            username: username
          }
        });
        return response.data;
    };
    const _deletePokemonFromWishlist = (userId, pokName) => {
        return axios.delete(`${url}/${userId}/wishlist`, {
            data: [pokName] 
        })
        .then((response) => response.data)
        .catch((error) => {
          throw new Error('Failed to delete Pokemon from wishlist: ' + error);
        });
      };
    const _addPokemonToWishlist = (userId, pokName) => {
        console.log("user service add pok")
        return axios.put(`${url}/${userId}/wishlist`, [pokName])
        .then((response) => response.data)
        .catch((error) => {
          throw new Error('Failed to add Pokemon to wishlist: ' + error);
        });
      };
      const _deletePokemonFromCatchlist = (userId, pokName) => {
        return axios.delete(`${url}/${userId}/catchlist`, {
            data: [pokName] 
        })
        .then((response) => response.data)
        .catch((error) => {
          throw new Error('Failed to delete Pokemon from catchlist: ' + error);
        });
      };
    const _addPokemonToCatchlist = (userId, pokName) => {
        console.log("user service add pok")
        return axios.put(`${url}/${userId}/catchlist`, [pokName])
        .then((response) => response.data)
        .catch((error) => {
          throw new Error('Failed to add Pokemon to catchlist: ' + error);
        });
      };

    const _fetchUsers = async (params) => {
        const response = await axios.get(url, {
        params: {
            username: params.username,
            isAdmin: params.isAdmin,
            id: params.id,
            
        }
    });

    if(!response) {
        console.log("hata");
        return;
    }
    return response.data;
    };

    const _deleteUserByUsername = async (name) => {
        try {
          const response = await axios.delete(`${url}/name/${name}`);
          return response.data;
        } catch (error) {
          console.error("An error occurred while deleting the User:", error);
          throw error; 
        }
    };
    const _updateUserById = async (id, updatedData) => {
        try {
          const response = await axios.put(`${url}/${id}`, updatedData);
          return response.data;
        } catch (error) {
          console.error("An error occurred while updating the User:", error);
          throw error;
        }
    };
    const _addUser = async (pokemon) => {
        try {
          const response = await axios.post(`${url}`, pokemon);
          if (response && response.data) {
            console.log(response.data);
            return response.data;
          }
          console.log("Error adding User");
        } catch (error) {
          console.error("An error occurred while adding the User:", error);
        }
    return null;
    };
    
    
    const _delete = async() => {
        const response = await axios.delete(
            url, {}
        );
        return response.data;
    };
    return {
        fetchUsers: _fetchUsers,
        delete: _delete,
        getUserByUsername: _getUserByUsername,
        getUserWishlist: _getUserWishlist,
        getUserCatchlist: _getUserCatchlist,
        deleteUserByUsername: _deleteUserByUsername,
        updateUserById: _updateUserById,
        addUser: _addUser,
        deletePokemonFromWishlist: _deletePokemonFromWishlist,
        addPokemonToWishlist: _addPokemonToWishlist,
        deletePokemonFromCatchlist: _deletePokemonFromCatchlist,
        addPokemonToCatchlist: _addPokemonToCatchlist,
        getUserById:_getUserById,
    };
})();

export default UserService;