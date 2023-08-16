import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './pages/Login';
import Users from './pages/Users';
import Pokemons from './pages/pokemons';
import UserProfile from './pages/UserProfile';
import Home from './pages/Home';

export default function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Login />} />
          <Route path="/users" element={<Users />} />
          <Route path="/userprofile" element={<UserProfile />} />
          <Route path="/poks" element={<Pokemons />} />
          <Route path="/home" element={<Home />} />
        </Routes>
      </div>
    </Router>
  );
}