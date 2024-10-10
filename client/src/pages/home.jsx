import { useEffect, useState } from "react";
import getApiData from "../service/genericGetService";
import useAuth from "../context/useAuth";

function Home() {
  const [data, setData] = useState("");
  const [displayRole, setDisplayRole] = useState(false);
  const [adminData, setAdminData] = useState("");

  const { isAuth, roles } = useAuth();

  console.log(isAuth + " " + roles);

  useEffect(() => {
    async function getData() {
      const result = await getApiData("private");
      setAdminData(result);
    }

    getData();
  }, []);

  console.log(JSON.stringify(adminData));

  function handleButtonClick() {
    async function getData() {
      const result = await getApiData("auth/roles");
      setData(result);
      setDisplayRole((prev) => !prev);
    }

    getData();
  }

  return (
    <>
      <div>This Home page is visible to all</div>
      {/* <div>These are the user roles: {JSON.stringify(data)}</div> */}

      <div>
        <p>display the role of the current user: </p>
        {displayRole ? <div>{JSON.stringify(data)}</div> : ""}
        <button onClick={handleButtonClick} className="p-4 bg-yellow-300">
          Click me
        </button>
      </div>
    </>
  );
}

export default Home;
