import React, { useState } from 'react';
import { Form, Input, Button, Alert } from 'antd';
import AuthService from '../service/AuthService';
import { useNavigate } from 'react-router-dom';
import "../designs/Login.css";
import PokemonIcon from "../icons/pokemon.png";

const Login = () => {
  const [errorMessage, setErrorMessage] = useState(null);
  const navigate = useNavigate();

  const onFinish = async (values) => {
    const { username, password } = values;
    const response = await AuthService.signin({ username, password });
    console.log("Success:", values);
    if (response) {
    navigate("/home", { state: { username: username,isAdmin: response.isAdmin } });
    } else {
      setErrorMessage('Login failed. Please check your credentials.');
    }
  };

  return (
        <div className="login-container">
          <div className="login-box">
            <img src={PokemonIcon} alt="Pokemon" className="pokemon-image" />
            <div className="pokedex-text">Pokédex</div>
            {errorMessage && <Alert message={errorMessage} type="error" style={{ marginBottom: '20px' }} />}
            <Form
              name="login"
              initialValues={{ remember: true }}
              onFinish={onFinish}
              className="login-form"
            >
              <Form.Item
                label="Username"
                name="username"
                rules={[{ required: true, message: 'Please input your username!' }]}
              >
                <Input />
              </Form.Item>
    
              <Form.Item
                label="Password"
                name="password"
                rules={[{ required: true, message: 'Please input your password!' }]}
              >
                <Input.Password />
              </Form.Item>
    
              <Form.Item>
                <Button type="primary" htmlType="submit" className="login-button">
                  Log in
                </Button>
              </Form.Item>
            </Form>
          </div>
        </div>
      );
    };

export default Login;
