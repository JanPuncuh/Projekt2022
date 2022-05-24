import React from 'react';
import './App.css';
import Header from "./components/Header";
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import Login from "./components/Login";
import Register from "./components/Register";
import Map from "./components/MapView";
import RoadInfos from "./components/RoadInfos";

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Header title="My application"/>
                <Routes>
                    <Route path="/" exact element={<Map/>}/>
                    <Route path="/register" exact element={<Register/>}/>
                    <Route path="/login" exact element={<Login/>}/>
                    <Route path="/scrapper" exact element={<RoadInfos/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
