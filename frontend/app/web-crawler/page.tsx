'use client'

import React, { useEffect } from 'react'
import { WebCrawlerProps } from '../types/webcrawler'
import axios from 'axios'
import {Alert, Button, Card, Form, Input, message, Skeleton, Space} from 'antd'
import Link from "next/link";
type FieldType = {
    url?: string
}
function WebCrawler() {
    const [form] = Form.useForm()
    const [loading, setLoading] = React.useState(false)
    const [error, setError] = React.useState('')
    const [result, setResult] = React.useState<WebCrawlerProps>(
        {} as WebCrawlerProps
    )

    const fetchData = async (values: FieldType) => {
        setError('')
        setLoading(true)
        setResult({} as WebCrawlerProps)
        await axios
            .get('/api/web-crawler', {
                params: values,
            })
            .then((res) => {
                setResult(res.data)
            })
            .catch((err) => {
                setError('Invalid URL. Please try again')
                setResult({} as WebCrawlerProps)
            })
            .finally(() => {
                setLoading(false)
            })
    }

    const demo1Handle = () => {
        form.setFieldValue("url", "https://www.zehrs.ca/large-grade-a-eggs/p/20812144001_EA");
    }

    const demo2Handle = () => {
        form.setFieldValue("url", "http://www.multifoodwindsor.com/ecommerce/grocery%E6%9D%82%E8%B4%A7/salted-duck-eggs-%E7%BA%A2%E5%BF%83%E9%B9%B9%E8%9B%8B.html");
    }

    return (
        <div className='min-h-screen flex flex-col items-center justify-center p-4'>
            <Card title='Web Crawler' className='w-1/2 '>
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
                        label='URL'
                        name='url'
                        rules={[
                            {
                                required: true,
                                message: 'Please input your url!',
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
                {result.time && (
                    <div className='mt-6'>
                        <p className='font-semibold'>Time:</p>
                        <p className='mb-4'>{result.time}</p>
                        <p className='font-semibold'>Content:</p>
                        <p>{result.htmlContents}</p>
                    </div>
                )}
            </Card>
        </div>
    )
}

export default WebCrawler
