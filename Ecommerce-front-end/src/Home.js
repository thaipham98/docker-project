import React from "react";
import { Row, Spin } from "antd";
import Product from "./Product";
import useViewAllProducts from "./hooks/useViewAllProducts";

// This component is used to show all products.
function Home() {
  const { data, isLoading } = useViewAllProducts();
  if (isLoading) {
    return <Spin />;
  }
  return (
    <div className="site-layout-content">
      <Row gutter={16}>
        {data?.Data?.map((item) => (
          <Product id={item.pid} name={item.product_name} detail={item.description} price={item.price} />
        ))}
      </Row>
    </div>
  );
}

export default Home;
