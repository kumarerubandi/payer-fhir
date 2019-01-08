package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

public class AddColumnTask extends BaseTableColumnTypeTask<AddColumnTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(AddColumnTask.class);


	@Override
	public void execute() throws SQLException {
		Set<String> columnNames = JdbcUtils.getColumnNames(getConnectionProperties(), getTableName());
		if (columnNames.contains(getColumnName())) {
			ourLog.info("Column {} already exists on table {} - No action performed", getColumnName(), getTableName());
			return;
		}

		String type = getSqlType();
		String nullable = getSqlNotNull();
		if (isNullable()) {
			nullable = "";
		}

		String sql = "alter table " + getTableName() + " add column " + getColumnName() + " " + type + " " + nullable;
		ourLog.info("Adding column {} of type {} to table {}", getColumnName(), type, getTableName());
		executeSql(sql);
	}

}
