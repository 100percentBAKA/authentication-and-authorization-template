import { Link } from "react-router-dom";

function Landing() {
  function allCookies() {
    console.log(document.cookie.split(";"));

    const cookies = document.cookie.split(";");

    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i];
      const eqPosition = cookie.indexOf("=");
      const cookieName =
        eqPosition > -1 ? cookie.substring(0, eqPosition) : cookie;
      document.cookie =
        cookieName + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/";
    }
  }

  return (
    <>
      <div>
        <p className="text-3xl">Welcome</p>
        <div className="flex flex-col space-y-4">
          <Link to="app/public">To access public page</Link>
          <Link to="app/private">If you are a pro user</Link>
          <Link to="app/admin">If you are the admin</Link>
        </div>
        <button onClick={allCookies}>Click</button>
      </div>
    </>
  );
}

export default Landing;
