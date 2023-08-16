import { set } from "lodash";
import React, {useEffect, useState, useCallback} from "react";
import UserService from "../service/UserService";
import { Table, Input } from "antd"; 
import "../designs/Users.css";
//import 'antd/dist/antd.css'; 

const PersonList = () => {
    const [showAddForm, setShowAddForm] = useState(false);
    const [showUpdateForm, setShowUpdateForm] = useState(false);
    const [currentRecord, setCurrentRecord] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [{isLoading, users , pagination}, setState] = useState({
        isLoading: true,
        users: [],
        error: null,
        pagination: {
            current: 1,
            pageSize: 5,
            total: 30
        },
    });
    const columns = [
        {
          title: 'Id',
          dataIndex: 'id',
          sorter:true,
          width: "10%",
        },
        {
          title: 'Username',
          dataIndex: 'username',
          width: "20%",
        },
        {
          title: 'Admin/Trainer',
          dataIndex: 'isAdmin',
          width: "20%",
          render: isAdmin => isAdmin ? 'Admin' : 'Trainer',
        },
        {
            title: 'Delete',
            dataIndex: '',
            key: 'x',
            render: (text, record) => (
              <button onClick={() => deleteUser(record.username)}>Delete</button>
            ),
          },
          {
            title: 'Update',
            dataIndex: '',
            key: 'x',
            render: (text, record) => (
              <button onClick={() => updateUser(record)}>Update</button>
            ),
          },
    
      ];
    const fetchUsers = useCallback(async () => {
        setState( (prevState) => {
            return{
                ...prevState, isLoading:true
            };
        });
        try {
            const data = await UserService.fetchUsers({pagination,name: searchTerm});
            setState( (prevState) => {
                return{
                    ...prevState, isLoading:false, users: data?.payload
                };
            });
        } catch (error) {
            console.log(error);
            setState( (prevState) => {
                return{
                    ...prevState, isLoading:false
                };
            });
        }
    },[pagination]);

    useEffect(()=> {
        console.log(pagination);
        fetchUsers({pagination});
  
    },[fetchUsers,pagination]);

    const handleTableChange = (pagination) => {
        setState((prevState) => {
            return {...prevState,pagination};
        });
    }
    const searchUsers = useCallback(async () => {
        setState((prevState) => {
            return { ...prevState, isLoading: true };
        });
        try {
            const data = await UserService.getUserByUsername(searchTerm);
            setState((prevState) => {
                return { ...prevState, isLoading: false, users: data?.payload };
            });
        } catch (error) {
            console.log(error);
            setState((prevState) => {
                return { ...prevState, isLoading: false };
            });
        }
    }, [searchTerm]);
    const updateUser = (record) => {
        setCurrentRecord(record); 
        setShowUpdateForm(true); 
      };
    const addUser = async (e) => {
        e.preventDefault();
        const newUserData = {
          username: e.target.username.value,
          password: e.target.password.value,
          isAdmin: e.target.isAdmin.checked,
        };
      
        try {
          await UserService.addUser(newUserData);
          fetchUsers({ pagination });
          setShowAddForm(false); 
        } catch (error) {
          console.log(error);
        }
      };
      const deleteUser = async (name) => {
        try {
          await UserService.deleteUserByUsername(name); 
          fetchUsers({ pagination });
        } catch (error) {
          console.log(error);
        }
      };

      return (
        <div className="users-container">
          <h2>User List</h2>
          <div className="main-container">
          <div className="search-container">
            <input
              type="text"
              placeholder="Search User by username"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <div>
              <button onClick={searchUsers}>Search</button>
              <button onClick={() => fetchUsers(pagination)}>Reload</button>
              <button onClick={() => setShowAddForm(true)}>Add User</button>
            </div>
          </div>
        <div className="table-with-forms-container">
          
          {showUpdateForm && (
      <div className="add-update-form">
        <h3>Update User</h3>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            const updatedData = {
              username: e.target.username.value,
             
            };
            UserService.updateUserById(currentRecord.id, updatedData)
              .then(() => {
                fetchUsers(pagination);
                setShowUpdateForm(false);
              })
              .catch((error) => {
                console.log(error);
              });
          }}
        >
          <label>
            UserName:
            <input type="text" name="username" defaultValue={currentRecord.username} />
          </label>
          
          <button type="submit">Save</button>
          <button type="button" onClick={() => setShowUpdateForm(false)}>Cancel</button>
        </form>
      </div>
    )}
    
          {showAddForm && (
            <div className="add-update-form">
              <h3>Add User</h3>
              <form onSubmit={addUser}>
                <label>
                  UserName:
                  <input type="text" name="username" required/>
                </label>
                <label>
                  Password:
                  <input type="text" name="password" required/>
                </label>
                <label>
                isAdmin:
                 <input type="checkbox" name="isAdmin" defaultChecked={false} />
                </label>
                <button type="submit">Add</button>
                <button type="button" onClick={() => setShowAddForm(false)}>Cancel</button>
              </form>
            </div>
          )}
          <div className="table-container">
            <Table
              columns={columns}
              rowKey={(record) => record.id}
              dataSource={users || []}
              loading={isLoading}
              pagination={pagination}
              onChange={handleTableChange}
            />
          </div>
          </div>
        </div>
        </div>
      );
    };

export default PersonList;