import React from "react";
import { Link } from "react-router-dom";

const Header = () => {
  return (
    <React.Fragment>
      <nav className="navbar navbar-default">
        <div className="navbar-header">
          <Link className="navbar-brand" to="/home">
            Google Drive API Accessor
          </Link>
        </div>
        <ul className="nav navbar-nav pull-right">
          <li>
            <Link to="/home">Home</Link>
          </li>
          <li>
            <Link to="/createFolder">Create Folder</Link>
          </li>
          <li>
            <Link to="/uploadFile">Upload File</Link>
          </li>
        </ul>
      </nav>
    </React.Fragment>
  );
};

export default Header;
