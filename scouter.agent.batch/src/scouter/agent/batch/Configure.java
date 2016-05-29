/*
 *  Copyright 2016 the original author or authors. 
 *  @https://github.com/scouter-project/scouter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 */
package scouter.agent.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


import scouter.agent.util.JarUtil;
import scouter.lang.conf.ConfigValueUtil;
import scouter.net.NetConstants;
import scouter.util.DateUtil;
import scouter.util.FileUtil;
import scouter.util.HashUtil;
import scouter.util.StringSet;
import scouter.util.StringUtil;

public class Configure {
	public static final String CONFIG_SCOUTER_ENABLED = "scouter_enabled";
	public static final String VM_SCOUTER_ENABLED = "scouter.enabled";
	public static boolean JDBC_REDEFINED = false;
	private static Configure instance = null;
	
	public Properties property = new Properties();
    private File propertyFile;
    public static String agent_dir_path;
    static {
    	agent_dir_path = JarUtil.getThisJarFile().getParent();
    }

    public final static synchronized Configure getInstance() {
		if (instance == null) {
			instance = new Configure();
		}
		return instance;
	}
    
    // Scouter enable/disable
    public boolean scouter_enabled = true;
    public boolean scouter_stop = false;
    
    // Batch basic configuration
    public String batch_id_type = ""; // Class, Args, Props 
    public String batch_id = "";
        
    // SQL
    public int sql_max_count = 100;
    
    // Thread Dump
    public long dump_interval_ms = 10000;
    public String [] dump_filter = null;
	public File dump_dir = new File(agent_dir_path + "/dump");
	public boolean dump_enabled = true;
	public boolean dump_header_exists = true;
	
	//Network
	public String net_local_udp_ip = null;
	public int net_local_udp_port;
	public String net_collector_ip = "127.0.0.1";
	public int net_collector_udp_port = NetConstants.SERVER_UDP_PORT;
	public int net_collector_tcp_port = NetConstants.SERVER_TCP_PORT;
	public int net_collector_tcp_session_count = 1;
	public int net_collector_tcp_so_timeout_ms = 60000;
	public int net_collector_tcp_connection_timeout_ms = 3000;
	public int net_udp_packet_max_bytes = 60000;
	public long net_udp_collection_interval_ms = 100;

	//Object
	public String obj_type = "batch";
	public String obj_name = "";
	public String obj_host_type = "";
	public String obj_host_name = "";
	public boolean obj_name_auto_pid_enabled = false;
	public boolean obj_type_inherit_to_child_enabled = false;

	//profile
	public boolean profile_http_querystring_enabled;
	public boolean profile_http_header_enabled;
	public String profile_http_header_url_prefix = "/";
	public boolean profile_http_parameter_enabled;
	public String profile_http_parameter_url_prefix = "/";
	public boolean profile_summary_mode_enabled = false;
	public boolean profile_thread_cputime_enabled = false;
	public boolean profile_socket_open_fullstack_enabled = false;
	public int profile_socket_open_fullstack_port = 0;
	public boolean profile_sqlmap_name_enabled = true;
	public boolean profile_connection_open_enabled = true;
	public boolean profile_connection_open_fullstack_enabled = false;
	public boolean profile_connection_autocommit_status_enabled = false;
	public boolean profile_method_enabled = true;
	public int profile_step_max_count = 1024;
	public boolean profile_fullstack_service_error_enabled = false;
	public boolean profile_fullstack_apicall_error_enabled = false;
	public boolean profile_fullstack_sql_error_enabled = false;
	public boolean profile_fullstack_sql_commit_enabled = false;
	public int profile_fullstack_max_lines = 0;
	public boolean profile_sql_escape_enabled = true;
	public boolean _profile_fullstack_sql_connection_enabled = false;
	public boolean profile_fullstack_rs_leak_enabled = false;
	public boolean profile_fullstack_stmt_leak_enabled = false;

	//Trace
	public int trace_user_mode = 2; // 0:Remote IP, 1:JSessionID, 2:SetCookie
	public boolean trace_background_socket_enabled = true;
	public String trace_service_name_header_key;
	public String trace_service_name_get_key;
	public String trace_service_name_post_key;
	public long trace_activeserivce_yellow_time = 3000;
	public long trace_activeservice_red_time = 8000;
	public String trace_http_client_ip_header_key = "";
	public boolean trace_interservice_enabled = false;
	public String _trace_interservice_gxid_header_key = "X-Scouter-Gxid";
	public boolean trace_response_gxid_enabled = false;
	public String _trace_interservice_callee_header_key = "X-Scouter-Callee";
	public String _trace_interservice_caller_header_key = "X-Scouter-Caller";
	public String trace_user_session_key = "JSESSIONID";
	public boolean _trace_auto_service_enabled = false;
	public boolean _trace_auto_service_backstack_enabled = true;
	public boolean trace_db2_enabled = true;
	public boolean trace_webserver_enabled = false;
	public String trace_webserver_name_header_key = "X-Forwarded-Host";
	public String trace_webserver_time_header_key = "X-Forwarded-Time";
	public int _trace_fullstack_socket_open_port = 0;

	public boolean trace_rs_leak_enabled = false;
	public boolean trace_stmt_leak_enabled = false;

	//Dir
	public File plugin_dir = new File(agent_dir_path + "/plugin");
	//public File mgr_agent_lib_dir = new File("./_scouter_");

	//Manager
	public String mgr_static_content_extensions = "js, htm, html, gif, png, jpg, css";
	public String mgr_log_ignore_ids = "";

	//Autodump
	public boolean autodump_enabled = false;
	public int autodump_trigger_active_service_cnt = 10000;
	public long autodump_interval_ms = 30000;
	public int autodump_level = 1; // 1:ThreadDump, 2:ActiveService, 3:ThreadList
	public int autodump_stuck_thread_ms = 0;

	//XLog
	public int xlog_lower_bound_time_ms = 0;
	public int xlog_error_jdbc_fetch_max = 10000;
	public int xlog_error_sql_time_max_ms = 30000;
	public boolean xlog_error_check_user_transaction_enabled = true;

	//Alert
	public int alert_message_length = 3000;
	public long alert_send_interval_ms = 10000;
	public int alert_perm_warning_pct = 90;

	//Log
	public boolean _log_asm_enabled;
	public boolean _log_udp_xlog_enabled;
	public boolean _log_udp_object_enabled;
	public boolean _log_udp_counter_enabled;
	public boolean _log_datasource_lookup_enabled = true;
	public boolean _log_background_sql = false;
	public String log_dir ="";
	public boolean log_rotation_enabled =true;
	public int log_keep_days =7;
	public boolean _trace = false;
    public boolean _trace_use_logger = false;

	//Hook
	public String hook_args_patterns = "";
	public String hook_return_patterns = "";
	public String hook_constructor_patterns = "";
	public String hook_connection_open_patterns = "";
	public String hook_context_classes = "javax/naming/InitialContext";
	public String hook_method_patterns = "";
	public String hook_method_ignore_prefixes = "get,set";
	public String hook_method_ignore_classes = "";
	public String hook_method_exclude_patterns = "";
	private StringSet _hook_method_ignore_classes = new StringSet();
	public boolean hook_method_access_public_enabled = true;
	public boolean hook_method_access_private_enabled = false;
	public boolean hook_method_access_protected_enabled = false;
	public boolean hook_method_access_none_enabled = false;
	public String hook_service_patterns = "";
	public String hook_apicall_patterns = "";
	public String hook_apicall_info_patterns = "";
	public String hook_jsp_patterns = "";
	public String hook_jdbc_pstmt_classes = "";
	public String hook_jdbc_stmt_classes = "";
	public String hook_jdbc_rs_classes = "";
	public String hook_jdbc_wrapping_driver_patterns = "";
	public String hook_add_fields = "";
	public boolean _hook_serivce_enabled = true;
	public boolean _hook_dbsql_enabled = true;
	public boolean _hook_dbconn_enabled = true;
	public boolean _hook_cap_enabled = true;
	public boolean _hook_methods_enabled = true;
	public boolean _hook_socket_enabled = true;
	public boolean _hook_jsp_enabled = true;
	public boolean _hook_async_enabled = true;
	public boolean _hook_usertx_enabled = true;
	public String _hook_direct_patch_classes = "";
	public boolean _hook_spring_rest_enabled = false;
	public String _hook_boot_prefix=null;

	//Control
	public boolean control_reject_service_enabled = false;
	public int control_reject_service_max_count = 10000;
	public boolean control_reject_redirect_url_enabled = false;
	public String control_reject_text = "too many request!!";
	public String control_reject_redirect_url = "/error.html";

	// Counter
	public boolean counter_enabled = true;
	public long counter_recentuser_valid_ms = DateUtil.MILLIS_PER_FIVE_MINUTE;
	public String counter_object_registry_path = "/tmp/scouter";

	// SFA(Stack Frequency Analyzer)
	public boolean sfa_dump_enabled = false;
	public int sfa_dump_interval_ms = 10000;

	//Summary
	public boolean summary_enabled = true;
	public boolean _summary_connection_leak_fullstack_enabled = false;
	public int _summary_service_max_count = 5000;
	public int _summary_sql_max_count = 5000;
	public int _summary_api_max_count = 5000;
	public int _summary_ip_max_count = 5000;
	public int _summary_useragent_max_count = 5000;
	public int _summary_error_max_count = 500;
	public int _summary_enduser_nav_max_count = 5000;
	public int _summary_enduser_ajax_max_count = 5000;
	public int _summary_enduser_error_max_count = 5000;
	
	//EndUser
	public String enduser_trace_endpoint_url = "/_scouter_browser.jsp";

	//Experimental(ignoreset)
	public boolean __experimental = false;
	public boolean __control_connection_leak_autoclose_enabled = false;
	public boolean __ip_dummy_test = false;

	//internal variables
	private int objHash;
	private String objName;
	private int objHostHash;
	private String objHostName;
	private Set<String> static_contents = new HashSet<String>();
	private StringSet log_ignore_set = new StringSet();
	private String[] _hook_method_ignore_prefix = null;
	private int _hook_method_ignore_prefix_len = 0;
	private int hook_signature;
	private int enduser_perf_endpoint_hash = HashUtil.hash(enduser_trace_endpoint_url);

	/**
	 * sometimes call by sample application, at that time normally set some
	 * properties directly
	 */
	private Configure() {
		Properties p = new Properties();
		Map args = new HashMap();
		args.putAll(System.getenv());
		args.putAll(System.getProperties());
		p.putAll(args);
		this.property = p;
		reload();
	}
	
	public File getPropertyFile() {
		if (propertyFile != null) {
			return propertyFile;
		}
		String s = System.getProperty("scouter.config", agent_dir_path + "/conf/scouter.batch.conf");
		propertyFile = new File(s.trim());
		return propertyFile;
	}

	public void reload() {
		File file = getPropertyFile();
		Properties temp = new Properties();
		if (file.canRead()) {
			FileInputStream in = null;
			try {
				in = new FileInputStream(file);
				temp.load(in);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				FileUtil.close(in);
			}
		}
		property = ConfigValueUtil.replaceSysProp(temp);
		apply();
		
		Logger.println(CONFIG_SCOUTER_ENABLED + "=" + this.scouter_enabled);		
	}

	private void apply() {
		// enable or disable
		this.scouter_enabled = getBoolean(CONFIG_SCOUTER_ENABLED, true);
		if(getValue(VM_SCOUTER_ENABLED) != null){
			this.scouter_enabled = getBoolean(VM_SCOUTER_ENABLED, true);
		}
		
		// start for batch
		this.batch_id_type = getValue("batch_id_type", "class");
		if("class".equals(this.batch_id_type)){
			this.batch_id = getValue("batch_id", "");
		}else if("args".equals(this.batch_id_type)){
			this.batch_id = getValue("batch_id", "0");		
		}else if("props".equals(this.batch_id_type)){
			this.batch_id = getValue("batch_id", "JobId");			
		}
		this.obj_name = getValue("obj_name", "batch");
		// end for batch
		
		// SQL
		this.sql_max_count = getInt("sql_max_count", 100);
		
		// Batch Dump
		this.dump_interval_ms = getInt("dump_interval_ms", 10000);
		if (this.dump_interval_ms < 5000) {
			this.dump_interval_ms = 5000;
		}
		String value = getValue("dump_filter");
		if(value != null){
			String [] arrs = StringUtil.split(value, ',');
			if(arrs != null && arrs.length > 0){
				ArrayList<String> lists = new ArrayList<String>();
				for(String line:arrs){
					line = line.trim();
					if(line.length() == 0){
						continue;
					}
					lists.add(line);
				}
				if(lists.size() > 0){
					this.dump_filter = new String[lists.size()];
					for(int i=0; i < lists.size();i++){
						this.dump_filter[i] = lists.get(i);
					}
				}
			}
		}
		
		value = getValue("dump_dir");
		if(value !=null){
			File dir = new File(value);
			if(!dir.exists() || !dir.isDirectory()){
				System.err.println("dump_dir(" + value + ") is not exists or not a directory");
			}
			this.dump_dir = dir;
		}
		this.dump_enabled = getBoolean("dump_enabled", true);		
		if(!dump_dir.canWrite()){
			this.dump_enabled = false;
			System.err.println("dump_dir(" + dump_dir.getAbsolutePath() + ") can't write");				
		}
		
		this.dump_header_exists = getBoolean("dump_header_exists", true);
		
		this.profile_http_querystring_enabled = getBoolean("profile_http_querystring_enabled", false);
		this.profile_http_header_enabled = getBoolean("profile_http_header_enabled", false);
		this.profile_http_parameter_enabled = getBoolean("profile_http_parameter_enabled", false);
		this.profile_summary_mode_enabled = getBoolean("profile_summary_mode_enabled", false);
		this.xlog_lower_bound_time_ms = getInt("xlog_lower_bound_time_ms", 0);
		this.trace_service_name_header_key = getValue("trace_service_name_header_key", null);
		this.trace_service_name_get_key = getValue("trace_service_name_get_key");
		this.trace_service_name_post_key = getValue("trace_service_name_post_key");
		this.dump_dir = new File(getValue("dump_dir", agent_dir_path + "/dump"));
		try {
			this.dump_dir.mkdirs();
		} catch (Exception e) {
		}
//		this.mgr_agent_lib_dir = new File(getValue("mgr_agent_lib_dir", "./_scouter_"));
//		try {
//			this.mgr_agent_lib_dir.mkdirs();
//		} catch (Exception e) {
//		}
		this.plugin_dir = new File(getValue("plugin_dir", agent_dir_path + "/plugin"));
		
		this.autodump_enabled = getBoolean("autodump_enabled", false);
		this.autodump_trigger_active_service_cnt = getInt("autodump_trigger_active_service_cnt", 10000);
		if (this.autodump_trigger_active_service_cnt < 1) {
			this.autodump_trigger_active_service_cnt = 1;
		}
		this.autodump_level = getInt("autodump_level", 1);
		this.autodump_interval_ms = getInt("autodump_interval_ms", 30000);
		if (this.autodump_interval_ms < 5000) {
			this.autodump_interval_ms = 5000;
		}
		this.autodump_stuck_thread_ms = getInt("autodump_stuck_thread_ms", 0);
		this.mgr_static_content_extensions = getValue("mgr_static_content_extensions", "js, htm, html, gif, png, jpg, css");
		this.profile_thread_cputime_enabled = getBoolean("profile_thread_cputime_enabled", false);
		this.profile_socket_open_fullstack_enabled = getBoolean("profile_socket_open_fullstack_enabled", false);
		this.trace_background_socket_enabled = getBoolean("trace_background_socket_enabled", true);
		this.profile_socket_open_fullstack_port = getInt("profile_socket_open_fullstack_port", 0);
		this.profile_sql_escape_enabled = getBoolean("profile_sql_escape_enabled", true);
		this.profile_sqlmap_name_enabled = getBoolean("profile_sqlmap_name_enabled", true);
		this.net_udp_packet_max_bytes = getInt("net_udp_packet_max_bytes", 60000);
		this.trace_activeserivce_yellow_time = getLong("trace_activeserivce_yellow_time", 3000);
		this.trace_activeservice_red_time = getLong("trace_activeservice_red_time", 8000);
		this.mgr_log_ignore_ids = getValue("mgr_log_ignore_ids", "");
		this.log_ignore_set = getStringSet("mgr_log_ignore_ids", ",");
		this._log_udp_xlog_enabled = getBoolean("_log_udp_xlog_enabled", false);
		this._log_udp_counter_enabled = getBoolean("_log_udp_counter_enabled", false);
		this._log_udp_object_enabled = getBoolean("_log_udp_object_enabled", false);
		this.net_local_udp_ip = getValue("net_local_udp_ip");
		this.net_local_udp_port = getInt("net_local_udp_port", 0);
		this.net_collector_ip = getValue("net_collector_ip", "127.0.0.1");
		this.net_collector_udp_port = getInt("net_collector_udp_port", NetConstants.SERVER_UDP_PORT);
		this.net_collector_tcp_port = getInt("net_collector_tcp_port", NetConstants.SERVER_TCP_PORT);
		this.net_collector_tcp_session_count = getInt("net_collector_tcp_session_count", 1, 1);
		this.net_collector_tcp_connection_timeout_ms = getInt("net_collector_tcp_connection_timeout_ms", 3000);
		this.net_collector_tcp_so_timeout_ms = getInt("net_collector_tcp_so_timeout_ms", 60000);
		this.hook_signature = 0;
		this.hook_args_patterns = getValue("hook_args_patterns", "");
		this.hook_return_patterns = getValue("hook_return_patterns", "");
		this.hook_constructor_patterns = getValue("hook_constructor_patterns", "");
		this.hook_connection_open_patterns = getValue("hook_connection_open_patterns", "");

		this._log_datasource_lookup_enabled = getBoolean("_log_datasource_lookup_enabled", true);
		this.profile_connection_open_enabled = getBoolean("profile_connection_open_enabled", true);
		this._summary_connection_leak_fullstack_enabled = getBoolean("_summary_connection_leak_fullstack_enabled", false);
		this.hook_method_patterns = getValue("hook_method_patterns", "");
		this.hook_method_exclude_patterns = getValue("hook_method_exclude_patterns", "");
		this.hook_method_access_public_enabled = getBoolean("hook_method_access_public_enabled", true);
		this.hook_method_access_protected_enabled = getBoolean("hook_method_access_protected_enabled", false);
		this.hook_method_access_private_enabled = getBoolean("hook_method_access_private_enabled", false);
		this.hook_method_access_none_enabled = getBoolean("hook_method_access_none_enabled", false);
		this.hook_method_ignore_prefixes = StringUtil.removeWhitespace(getValue("hook_method_ignore_prefixes", "get,set"));
		this._hook_method_ignore_prefix = StringUtil.split(this.hook_method_ignore_prefixes, ",");
		this._hook_method_ignore_prefix_len = this._hook_method_ignore_prefix == null ? 0
				: this._hook_method_ignore_prefix.length;
		this.hook_method_ignore_classes = StringUtil.trimEmpty(StringUtil.removeWhitespace(getValue(
				"hook_method_ignore_classes", "")));
		this._hook_method_ignore_classes = new StringSet(StringUtil.tokenizer(
				this.hook_method_ignore_classes.replace('.', '/'), ","));
		this.profile_method_enabled = getBoolean("profile_method_enabled", true);
		this.hook_service_patterns = getValue("hook_service_patterns", "");
		this.hook_apicall_patterns = getValue("hook_apicall_patterns", "");
		this.hook_apicall_info_patterns = getValue("hook_apicall_info_patterns", "");
		this.hook_jsp_patterns = getValue("hook_jsp_patterns", "");
		
		this.hook_jdbc_pstmt_classes = getValue("hook_jdbc_pstmt_classes", "");
		this.hook_jdbc_stmt_classes = getValue("hook_jdbc_stmt_classes", "");
		this.hook_jdbc_rs_classes = getValue("hook_jdbc_rs_classes", "");
		this.hook_jdbc_wrapping_driver_patterns = getValue("hook_jdbc_wrapping_driver_patterns", "");
		this.hook_add_fields = getValue("hook_add_fields", "");
		this.hook_context_classes = getValue("hook_context_classes", "javax/naming/InitialContext");
		
		this.hook_signature ^= this.hook_args_patterns.hashCode();
		this.hook_signature ^= this.hook_return_patterns.hashCode();
		this.hook_signature ^= this.hook_constructor_patterns.hashCode();
		this.hook_signature ^= this.hook_connection_open_patterns.hashCode();
		this.hook_signature ^= this.hook_method_patterns.hashCode();
		this.hook_signature ^= this.hook_service_patterns.hashCode();
		this.hook_signature ^= this.hook_apicall_patterns.hashCode();
		this.hook_signature ^= this.hook_jsp_patterns.hashCode();
		this.hook_signature ^= this.hook_jdbc_wrapping_driver_patterns.hashCode();
		
		this.control_reject_service_enabled = getBoolean("control_reject_service_enabled", false);
		this.control_reject_service_max_count = getInt("control_reject_service_max_count", 10000);
		this.control_reject_redirect_url_enabled = getBoolean("control_reject_redirect_url_enabled", false);
		this.control_reject_text = getValue("control_reject_text", "too many request!!");
		this.control_reject_redirect_url = getValue("control_reject_redirect_url", "/error.html");

		this.profile_step_max_count = getInt("profile_step_max_count", 1024);
		if (this.profile_step_max_count < 100)
			this.profile_step_max_count = 100;
		this._log_background_sql = getBoolean("_log_background_sql", false);
		this.profile_fullstack_service_error_enabled = getBoolean("profile_fullstack_service_error_enabled", false);
		this.profile_fullstack_apicall_error_enabled = getBoolean("profile_fullstack_apicall_error_enabled", false);
		this.profile_fullstack_sql_error_enabled = getBoolean("profile_fullstack_sql_error_enabled", false);
		this.profile_fullstack_sql_commit_enabled = getBoolean("profile_fullstack_sql_commit_enabled", false);
		this.profile_fullstack_max_lines = getInt("profile_fullstack_max_lines", 0);
		this.profile_fullstack_rs_leak_enabled = getBoolean("profile_fullstack_rs_leak_enabled", false);
		this.profile_fullstack_stmt_leak_enabled = getBoolean("profile_fullstack_stmt_leak_enabled", false);

		this.net_udp_collection_interval_ms = getInt("net_udp_collection_interval_ms", 100);
		this.profile_http_parameter_url_prefix = getValue("profile_http_parameter_url_prefix", "/");
		this.profile_http_header_url_prefix = getValue("profile_http_header_url_prefix", "/");
		this.trace_http_client_ip_header_key = getValue("trace_http_client_ip_header_key", "");
		this.trace_interservice_enabled = getBoolean("trace_interservice_enabled", false);
		this.trace_response_gxid_enabled = getBoolean("trace_response_gxid_enabled", false);
		this._trace_interservice_gxid_header_key = getValue("_trace_interservice_gxid_header_key", "X-Scouter-Gxid");
		this._trace_interservice_callee_header_key = getValue("_trace_interservice_callee_header_key", "X-Scouter-Callee");
		this._trace_interservice_caller_header_key = getValue("_trace_interservice_caller_header_key", "X-Scouter-Caller");
		this.profile_connection_open_fullstack_enabled = getBoolean("profile_connection_open_fullstack_enabled", false);
		this.profile_connection_autocommit_status_enabled = getBoolean("profile_connection_autocommit_status_enabled", false);
		this.trace_user_mode = getInt("trace_user_mode", 2);
		this.trace_user_session_key = getValue("trace_user_session_key", "JSESSIONID");
		this._trace_auto_service_enabled = getBoolean("_trace_auto_service_enabled", false);
		this._trace_auto_service_backstack_enabled = getBoolean("_trace_auto_service_backstack_enabled", true);
		this.counter_enabled = getBoolean("counter_enabled", true);
		this._hook_serivce_enabled = getBoolean("_hook_serivce_enabled", true);
		this._hook_dbsql_enabled = getBoolean("_hook_dbsql_enabled", true);
		this._hook_dbconn_enabled = getBoolean("_hook_dbconn_enabled", true);
		this._hook_cap_enabled = getBoolean("_hook_cap_enabled", true);
		this._hook_methods_enabled = getBoolean("_hook_methods_enabled", true);
		this._hook_socket_enabled = getBoolean("_hook_socket_enabled", true);
		this._hook_jsp_enabled = getBoolean("_hook_jsp_enabled", true);
		this._hook_async_enabled = getBoolean("_hook_async_enabled", true);
		this.trace_db2_enabled = getBoolean("trace_db2_enabled", true);
		this._hook_usertx_enabled = getBoolean("_hook_usertx_enabled", true);
		this._hook_direct_patch_classes = getValue("_hook_direct_patch_classes", "");
		this._hook_boot_prefix = getValue("_hook_boot_prefix");
		this.counter_recentuser_valid_ms = getLong("counter_recentuser_valid_ms", DateUtil.MILLIS_PER_FIVE_MINUTE);
		this.counter_object_registry_path = getValue("counter_object_registry_path", "/tmp/scouter");
		this.sfa_dump_enabled = getBoolean("sfa_dump_enabled", false);
		this.sfa_dump_interval_ms = getInt("sfa_dump_interval_ms", 10000);
		// 웹시스템으로 부터 WAS 사이의 성능과 어떤 웹서버가 요청을 보내 왔는지를 추적하는 기능을 ON/OFF하고
		// 관련 키정보를 지정한다.
		this.trace_webserver_enabled = getBoolean("trace_webserver_enabled", false);
		this.trace_webserver_name_header_key = getValue("trace_webserver_name_header_key", "X-Forwarded-Host");
		this.trace_webserver_time_header_key = getValue("trace_webserver_time_header_key", "X-Forwarded-Time");

		this.trace_rs_leak_enabled = getBoolean("trace_rs_leak_enabled", false);
		this.trace_stmt_leak_enabled = getBoolean("trace_stmt_leak_enabled", false);

		// SUMMARY최대 갯수를 관리한다.
		this.summary_enabled = getBoolean("summary_enabled", true);
		this._summary_sql_max_count = getInt("_summary_sql_max_count", 5000);
		this._summary_api_max_count = getInt("_summary_api_max_count", 5000);
		this._summary_service_max_count = getInt("_summary_service_max_count", 5000);
		this._summary_ip_max_count = getInt("_summary_ip_max_count", 5000);
		this._summary_useragent_max_count = getInt("_summary_useragent_max_count", 5000);
		this._summary_error_max_count = getInt("_summary_error_max_count", 500);
		
		this._summary_enduser_nav_max_count = getInt("_summary_enduser_nav_max_count", 5000);
		this._summary_enduser_ajax_max_count = getInt("_summary_enduser_ajax_max_count", 5000);
		this._summary_enduser_error_max_count = getInt("_summary_enduser_error_max_count", 5000);

		//Experimental(ignoreset)
		this.__experimental = getBoolean("__experimental", false);
		this.__control_connection_leak_autoclose_enabled = getBoolean("__control_connection_leak_autoclose_enabled", false);

        //For testing
        this.__ip_dummy_test = getBoolean("__ip_dummy_test", false);

		this.alert_perm_warning_pct = getInt("alert_perm_warning_pct", 90);
		this._hook_spring_rest_enabled = getBoolean("_hook_spring_rest_enabled", false);
		this.alert_message_length = getInt("alert_message_length", 3000);
		this.alert_send_interval_ms = getInt("alert_send_interval_ms", 10000);
		this.xlog_error_jdbc_fetch_max = getInt("xlog_error_jdbc_fetch_max", 10000);
		this.xlog_error_sql_time_max_ms = getInt("xlog_error_sql_time_max_ms", 30000);
		this._log_asm_enabled = getBoolean("_log_asm_enabled", false);
		this.obj_type_inherit_to_child_enabled = getBoolean("obj_type_inherit_to_child_enabled", false);
		this._profile_fullstack_sql_connection_enabled = getBoolean("_profile_fullstack_sql_connection_enabled", false);
		this._trace_fullstack_socket_open_port = getInt("_trace_fullstack_socket_open_port", 0);
		this.log_dir = getValue("log_dir", "");
		this.log_rotation_enabled = getBoolean("log_rotation_enabled", true);
		this.log_keep_days = getInt("log_keep_days", 7);
        this._trace = getBoolean("_trace", false);
        this._trace_use_logger = getBoolean("_trace_use_logger", false);
		
		this.enduser_trace_endpoint_url = getValue("enduser_trace_endpoint_url", "_scouter_browser.jsp");
		this.enduser_perf_endpoint_hash = HashUtil.hash(this.enduser_trace_endpoint_url);
		
		this.xlog_error_check_user_transaction_enabled = getBoolean("xlog_error_check_user_transaction_enabled", true);
			
		//resetObjInfo();
		setStaticContents();
	}

	public int getObjHash() {
		return this.objHash;
	}

	public String getObjName() {
		return this.objName;
	}

	public int getObjHostHash(){
		return this.objHostHash;
	}

	public String getObjHostName() {
		return this.objHostName;
	}

	public int getEndUserPerfEndpointHash() {
		return this.enduser_perf_endpoint_hash;
	}

	public boolean isIgnoreLog(String id) {
		return log_ignore_set.hasKey(id);
	}

	private StringSet getStringSet(String key, String deli) {
		StringSet set = new StringSet();
		String v = getValue(key);
		if (v != null) {
			String[] vv = StringUtil.split(v, deli);
			for (String x : vv) {
				x = StringUtil.trimToEmpty(x);
				if (x.length() > 0)
					set.put(x);
			}
		}
		return set;
	}
	private void setStaticContents() {
		Set<String> tmp = new HashSet<String>();
		String[] s = StringUtil.split(this.mgr_static_content_extensions, ',');
		for (int i = 0; i < s.length; i++) {
			String ss = s[i].trim();
			if (ss.length() > 0) {
				tmp.add(ss);
			}
		}
		static_contents = tmp;
	}
	public boolean isStaticContents(String content) {
		return static_contents.contains(content);
	}
	public boolean isIgnoreMethodPrefix(String name) {
		for (int i = 0; i < this._hook_method_ignore_prefix_len; i++) {
			if (name.startsWith(this._hook_method_ignore_prefix[i]))
				return true;
		}
		return false;
	}
	public boolean isIgnoreMethodClass(String classname) {
		return _hook_method_ignore_classes.hasKey(classname);
	}
	
	public String getValue(String key) {
		return StringUtil.trim(property.getProperty(key));
	}
	public String getValue(String key, String def) {
		return StringUtil.trim(property.getProperty(key, def));
	}
	public int getInt(String key, int def) {
		try {
			String v = getValue(key);
			if (v != null)
				return Integer.parseInt(v);
		} catch (Exception e) {
		}
		return def;
	}
	public int getInt(String key, int def, int min) {
		try {
			String v = getValue(key);
			if (v != null) {
				return Math.max(Integer.parseInt(v), min);
			}
		} catch (Exception e) {
		}
		return Math.max(def, min);
	}
	public long getLong(String key, long def) {
		try {
			String v = getValue(key);
			if (v != null)
				return Long.parseLong(v);
		} catch (Exception e) {
		}
		return def;
	}
	public boolean getBoolean(String key, boolean def) {
		try {
			String v = getValue(key);
			if (v != null)
				return Boolean.parseBoolean(v);
		} catch (Exception e) {
		}
		return def;
	}
	public String loadText() {
		File file = getPropertyFile();
		InputStream fin = null;
		try {
			fin = new FileInputStream(file);
			byte[] buff = FileUtil.readAll(fin);
			return new String(buff);
		} catch (Exception e) {
		} finally {
			FileUtil.close(fin);
		}
		return null;
	}
	public boolean saveText(String text) {
		File file = getPropertyFile();
		OutputStream out = null;
		try {
			if (file.getParentFile().exists() == false) {
				file.getParentFile().mkdirs();
			}
			out = new FileOutputStream(file);
			out.write(text.getBytes());
			return true;
		} catch (Exception e) {
		} finally {
			FileUtil.close(out);
		}
		return false;
	}
	public void printConfig() {
		Logger.info("Configure -Dscouter.config=" + propertyFile);
	}
	private static HashSet<String> ignoreSet = new HashSet<String>();
	static {
		ignoreSet.add("property");
		ignoreSet.add("__experimental");
	}

	public int getHookSignature() {
		return this.hook_signature;
	}

}