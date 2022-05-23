import React from 'react';
import MapView from './components/MapView';
import './App.css';
import Header from "./components/Header";
import { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { UserContext } from "./userContext";

import Login from "./components/Login";
import Register from "./components/Register";

import Map from "./components/MapView";
function App() {
  return (
    <BrowserRouter>

        <div className="App">
          <Header title="My application"></Header>
          <Routes>
            <Route path="/" exact element={<Map />}></Route>
            <Route path="/register" exact element={<Register />}></Route>
            <Route path="/login" exact element={<Login />}></Route>
          </Routes>
        </div>
    </BrowserRouter>
  );
}

export default App;
