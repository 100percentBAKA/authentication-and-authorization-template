import useAuth from "../context/useAuth";

function Home() {
  const { userEmail: email } = useAuth();
  console.log(email);
  return <div>This Home page is visible to all</div>;
}

export default Home;
