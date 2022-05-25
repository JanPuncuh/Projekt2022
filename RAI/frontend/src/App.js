import React from 'react';
import './App.css';
import Header from "./components/Header";
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import Login from "./components/Login";
import Logout from "./components/Logout";
import Register from "./components/Register";
import Map from "./components/MapView";
import RoadInfos from "./components/RoadInfos";
import Sessions from "./components/Sessions";
import { UserContext } from "./userContext";
import { useState, useEffect } from 'react';

function App() {

    const [user, setUser] = useState(localStorage.user ? JSON.parse(localStorage.user) : null);
    const updateUserData = (userInfo) => {
      localStorage.setItem("user", JSON.stringify(userInfo));
      setUser(userInfo);
    }
    
    return (
        <BrowserRouter>
          <UserContext.Provider value={{
            user: user,
            setUserContext: updateUserData
          }}>
            <div className="App">
              <Header title="My application"></Header>
              <Routes>
                     <Route path="/" exact element={<Map/>}/>
                     <Route path="/sessions" exact element={<Sessions/>}/>
                    <Route path="/map" exact element={<Map/>}/>
                    <Route path="/register" exact element={<Register/>}/>
                    <Route path="/login" exact element={<Login/>}/>
                    <Route path="/logout" exact element={<Logout/>}/>
                    <Route path="/scrapper" exact element={<RoadInfos/>}/>
              </Routes>
            </div>
          </UserContext.Provider>
        </BrowserRouter>
      );
    }

export default App;
