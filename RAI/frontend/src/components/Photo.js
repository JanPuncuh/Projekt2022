import moment from 'moment';
import { Link } from "react-router-dom";
function Photo(props){

    var date = moment(props.photo.date);
    var dateComponent = date.utc().format('HH:mm:ss - DD-MM-YYYY');
    return (
        <div className="card text-dark mb-2">
            <div >     
                <h5 className="card-title">{props.photo.name}</h5>
                <h5 className="card-title">{props.photo.views}</h5>
                <h5 className="card-title">{props.photo.likes}</h5>
                <h5 className="card-title">{props.photo.postedBy.username}</h5>
                <h5 className="card-title">{dateComponent}</h5>
                
            </div>
            <img  width="100px" src={"http://localhost:3001/"+props.photo.path} alt={props.photo.name}/>
            <input style={{width: "50px"}} class="btn btn-primary" 
                  type="submit" name="Like" value="Like"/>
                  <Link to={`/photo/${props.photo._id}`}>View Post</Link>
        </div>
    );
}

export default Photo;