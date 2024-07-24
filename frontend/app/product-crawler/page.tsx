'use client'

import React from 'react'
import axios from 'axios'
import {Alert, Button, Card, Form, Input, message, Skeleton, Space, Typography } from 'antd'
import Link from "next/link";
import {FoodItemType} from "@/app/types/food";
import Image from "next/image";


const { Text } = Typography;

type FieldType = {
    q?: string
}
function ProductCrawler() {
    const [form] = Form.useForm()
    const [loading, setLoading] = React.useState(false)
    const [error, setError] = React.useState('')
    const [result, setResult] = React.useState<FoodItemType[]>(
        {} as FoodItemType[]
    )

    const fetchData = async (values: FieldType) => {
        setError('')
        setLoading(true)
        setResult({} as FoodItemType[])
        await axios
            .get('/api/product/scraping', {
                params: values,
            })
            .then((res) => {
                setResult(res.data)
            })
            .catch((err) => {
                setError('Invalid URL. Please try again')
                setResult({} as FoodItemType[])
            })
            .finally(() => {
                setLoading(false)
            })
    }

    const demo1Handle = () => {
        form.setFieldValue("q", "coffee");
    }

    const demo2Handle = () => {
        form.setFieldValue("q", "beef");
    }

    const getCurrentMoment = () => {
        const date = new Date();
        return date.toLocaleString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: true });;
    }

    // @ts-ignore
    return (
        <div className='min-h-screen flex flex-col items-center justify-center p-4'>
            <Card title='Product Crawler' className='w-1/2 '>
                <Space size={'small'} direction={'horizontal'} className={'pb-2 px-4'} >
                    <Link href={'#demo1'} onClick={demo1Handle}>
                        Demo 1
                    </Link>
                    <Link href={'#demo2'} onClick={demo2Handle}>
                        Demo 2
                    </Link>
                </Space>
                <Form
                    name='basic'
                    labelCol={{ span: 3 }}
                    initialValues={{ remember: true }}
                    onFinish={fetchData}
                    // onFinishFailed={onFinishFailed}
                    autoComplete='off'
                    form={form}
                >
                    <Form.Item<FieldType>
                        label='Keyword'
                        name='q'
                        rules={[
                            {
                                required: true,
                                message: 'Please input your keyword!',
                            },
                        ]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item className='text-center'>
                        <Button type='primary' htmlType='submit' loading={loading} >
                            Submit
                        </Button>
                    </Form.Item>
                </Form>
                {loading && <Skeleton loading />}
                {error && <Alert message={error} type='error' />}
                {result?.length > 0 && (
                    <Space size={'small'} direction={'vertical'}>
                        <Text code={true}>Time checked: { getCurrentMoment() }</Text>
                        { result.map((item, index) =>  {
                            return (
                                <Card key={index}>
                                    <div className='flex'>
                                        <div className='mt-6 w-1/5'>
                                            <Image  src={item.image} alt={item.name} width={'80'} height={'80'} />
                                        </div>
                                        <div className='mt-6 w-4/5'>
                                            <p className='font-semibold'>Store:</p>
                                            <p className='mb-4'>{item.store}</p>
                                            <p className='font-semibold'>Name:</p>
                                            <p className='mb-4'>{item.name}</p>
                                            <p className='font-semibold'>Price:</p>
                                            <p className='mb-4'>CA${item.price}</p>
                                            <p className='font-semibold'>URL:</p>
                                            <p className='mb-4'>{item.url}</p>
                                        </div>
                                    </div>

                                </Card>
                            )
                        })}
                    </Space>
                )}
            </Card>
        </div>
    )
}

export default ProductCrawler
