import React, {useState, useEffect} from 'react';
import RoadInfo from './RoadInfo';

function RoadInfos() {
    const [roads, setRoads] = useState([]);

    const scrapInfo = async function () {
        const res = await fetch("http://localhost:3001/cesta/scrapper");
    }

    const getRoads = async function () {
        const res = await fetch("http://localhost:3001/cesta/scrapper/list");
        const data = await res.json();
        console.log(data)
        setRoads(data);
    }

    useEffect(function () {
        scrapInfo()
        getRoads();
    }, []);

    return (
        <>
            {roads.map(road => (<RoadInfo road={road} key={road._id}/>))}
        </>
    );
}

export default RoadInfos;