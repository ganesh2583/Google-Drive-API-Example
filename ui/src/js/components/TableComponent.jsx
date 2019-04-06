import React from "react";
import { Link } from "react-router-dom";

const TableComponent = props => {
  return (
    <React.Fragment>
      <table className="table table-hover">
        <thead>
          <tr>
            <th>Name</th>
            <th>Type</th>
          </tr>
        </thead>
        <tbody>
          {props.listOfFiles.map((item, key) => {
            return (
              <tr key={item.id}>
                <td>
                  {item.mimeType === "application/vnd.google-apps.folder" && (
                    <Link to={`/home/${item.name}`}>{item.name}</Link>
                  )}
                  {item.mimeType !== "application/vnd.google-apps.folder" &&
                    item.name}
                </td>
                <td>{item.mimeType}</td>
                <td>
                  <Link
                    to="/home"
                    onClick={() => props.download(item)}
                    download
                  >
                    Download
                  </Link>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
      {props.listOfFiles.length === 0 && (
        <div className="alert alert-warning">
          <strong>No Files in the Folder.</strong>
        </div>
      )}
    </React.Fragment>
  );
};

export default TableComponent;
